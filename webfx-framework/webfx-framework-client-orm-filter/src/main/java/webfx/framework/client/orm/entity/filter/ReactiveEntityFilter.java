package webfx.framework.client.orm.entity.filter;

import javafx.application.Platform;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import webfx.framework.client.orm.entity.filter.reactive_call.query.ReactiveQuery;
import webfx.framework.client.orm.entity.filter.reactive_call.querypush.ReactiveQueryOptionalPush;
import webfx.framework.client.orm.entity.filter.reactive_call.querypush.ReactiveQueryPush;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.entity.*;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.builder.ReferenceResolver;
import webfx.framework.shared.orm.expression.builder.ThreadLocalReferenceResolver;
import webfx.framework.shared.orm.expression.sqlcompiler.sql.SqlCompiled;
import webfx.framework.shared.orm.expression.terms.Alias;
import webfx.framework.shared.orm.expression.terms.As;
import webfx.framework.shared.orm.mapping.query_to_entity.QueryResultToEntitiesMapper;
import webfx.kit.util.properties.Properties;
import webfx.platform.shared.services.json.JsonObject;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.Numbers;
import webfx.platform.shared.util.Strings;
import webfx.platform.shared.util.async.Handler;
import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.util.function.Callable;
import webfx.platform.shared.util.function.Converter;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class ReactiveEntityFilter<E extends Entity> implements /*HasActiveProperty,*/ HasEntityStore {

    private final ReactiveEntityFilter<?> parentFilter;
    protected final List<ObservableValue<EqlFilter>> eqlFilterProperties = new ArrayList<>();
    private boolean eqlFiltersChangedSinceLastFetch;
    protected final ObjectProperty<EntityList<E>> entityListProperty = new SimpleObjectProperty<EntityList<E>/*GWT*/>() {
        @Override
        protected void invalidated() {
            scheduleOnEntityListChanged();
        }
    };
    private List<E> restrictedFilterList;
    private DataSourceModel dataSourceModel;
    private EntityStore store;
    private Object domainClassId;
    private EqlFilter baseFilter;
    protected Handler<EntityList<E>> entitiesHandler;
    private ReferenceResolver rootAliasReferenceResolver;
    private static int filterCount = 0;
    protected Object listId = "filter-" + ++filterCount;

    private final ReactiveQueryOptionalPush reactiveQueryOptionalPush;

    /*=================================
      ========= Constructors ==========
      =================================*/

    public ReactiveEntityFilter() {
        this(null);
    }

    public ReactiveEntityFilter(Object jsonOrClass) {
        this(null, jsonOrClass);
    }

    public ReactiveEntityFilter(ReactiveEntityFilter<?> parentFilter) {
        this.parentFilter = parentFilter;
        ReactiveQueryPush parentReactiveQueryPush = parentFilter == null ? null : parentFilter.reactiveQueryOptionalPush.getReactiveQueryPush();
        reactiveQueryOptionalPush = new ReactiveQueryOptionalPush(new ReactiveQuery(), new ReactiveQueryPush(parentReactiveQueryPush));
        reactiveQueryOptionalPush.setArgumentFetcher(this::queryArgumentFetcher);
        reactiveQueryOptionalPush.resultProperty().addListener((observableValue, oldQueryResult, newQueryResult) -> onNewQueryResult(newQueryResult));
    }

    public ReactiveEntityFilter(ReactiveEntityFilter<?> parentFilter, Object jsonOrClass) {
        this(parentFilter);
        combine(new EqlFilterBuilder(jsonOrClass));
    }

    /*=============================
      ======== Fluent API =========
      =============================*/

    public ReactiveEntityFilter<E> setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        dataSourceModel.getDomainModel(); // Ensuring the data model is loaded with formats registered before expression columns are set
        return this;
    }

    public ReactiveEntityFilter<E> setStore(EntityStore store) {
        this.store = store;
        return this;
    }

    public ReactiveEntityFilter<E> setListId(Object listId) {
        this.listId = listId;
        return this;
    }

    public ReactiveEntityFilter<E> setRestrictedFilterList(List<E> restrictedFilterList) {
        this.restrictedFilterList = restrictedFilterList;
        return this;
    }

    public ReactiveEntityFilter<E> setEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        this.entitiesHandler = entitiesHandler;
        return this;
    }

    public ReactiveEntityFilter<E> bindActivePropertyTo(ObservableValue<Boolean> activeProperty) {
        reactiveQueryOptionalPush.bindActivePropertyTo(activeProperty);
        return this;
    }

    public ReactiveEntityFilter<E> combine(String eqlFilterString) {
        return combine(EqlFilter.parse(eqlFilterString));
    }

    public ReactiveEntityFilter<E> combine(JsonObject json) {
        return combine(new EqlFilter(json));
    }

    public ReactiveEntityFilter<E> combine(EqlFilterBuilder eqlFilterBuilder) {
        return combine(eqlFilterBuilder.build());
    }

    public ReactiveEntityFilter<E> combine(EqlFilter eqlFilter) {
        if (domainClassId == null) {
            domainClassId = eqlFilter.getDomainClassId();
            baseFilter = eqlFilter;
        }
        return combine(new SimpleObjectProperty<>(eqlFilter));
    }

    public ReactiveEntityFilter<E> combine(ObservableValue<EqlFilter> eqlFilterProperty) {
        eqlFilterProperties.add(eqlFilterProperty);
        markEqlFiltersAsChanged();
        return this;
    }

    public <T> ReactiveEntityFilter<E> combine(ObservableValue<T> property, Converter<T, String> toEqlFilterStringConverter) {
        return combine(Properties.compute(property, t -> {
            // Calling the converter to get the eqlFilterString filter as String
            String eqlFilterString = toEqlFilterStringConverter.convert(t);
            // Converting it to a EqlFilter that we will return. If different from last value, this will trigger a global change check
            EqlFilter eqlFilter = EqlFilter.parse(eqlFilterString);
            // However it's possible that the EqlFilter hasn't changed but contains parameters that have changed (ex: name like ?search)
            // In that case (EqlFilter with parameter), we always schedule a global change check (which will consider parameters)
            //if (eqlFilterString != null && eqlFilterString.contains("?")) // Simple parameter test with ?
            markEqlFiltersAsChanged();
            return eqlFilter;
        }));
    }

    public <T> ReactiveEntityFilter<E> combineIfNotNull(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return combineIfNotNullOtherwise(property, toJsonFilterConverter, null);
    }

    public <T> ReactiveEntityFilter<E> combineIfNotNullOtherwiseForceEmptyResult(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return combineIfNotNullOtherwise(property, toJsonFilterConverter, "{where: 'false'}");
    }

    public <T> ReactiveEntityFilter<E> combineIfNotNullOtherwise(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter, String otherwiseEqlFilterString) {
        return combine(property, value -> value == null ? otherwiseEqlFilterString : toJsonFilterConverter.convert(value));
    }

    public ReactiveEntityFilter<E> combineIfNotEmpty(ObservableValue<String> property, Converter<String, String> toJsonFilterConverter) {
        return combine(property, s -> Strings.isEmpty(s) ? null : toJsonFilterConverter.convert(s));
    }

    public ReactiveEntityFilter<E> combineIfNotEmptyTrim(ObservableValue<String> property, Converter<String, String> toJsonFilterConverter) {
        return combineIfNotEmpty(property, s -> toJsonFilterConverter.convert(Strings.trim(s)));
    }

    public <T extends Number> ReactiveEntityFilter<E> combineIfPositive(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return combine(property, value -> Numbers.isPositive(value) ? toJsonFilterConverter.convert(value) : null);
    }

    public ReactiveEntityFilter<E> combineIfTrue(ObservableValue<Boolean> property, Callable<String> toJsonFilterConverter) {
        return combine(property, value -> Booleans.isTrue(value) ? toJsonFilterConverter.call() : null);
    }

    public ReactiveEntityFilter<E> combineIfFalse(ObservableValue<Boolean> property, Callable<String> toJsonFilterConverter) {
        return combine(property, value -> Booleans.isFalse(value) ? toJsonFilterConverter.call() : null);
    }

    public ReactiveEntityFilter<E> combine(ObservableValue<Boolean> ifProperty, EqlFilterBuilder eqlFilterBuilder) {
        return combine(ifProperty, eqlFilterBuilder.build());
    }

    public ReactiveEntityFilter<E> combine(ObservableValue<Boolean> ifProperty, String eqlFilterString) {
        return combine(ifProperty, EqlFilter.parse(eqlFilterString));
    }

    public ReactiveEntityFilter<E> combine(ObservableValue<Boolean> ifProperty, EqlFilter eqlFilter) {
        return combine(Properties.compute(ifProperty, value -> {
            markEqlFiltersAsChanged();
            return value ? eqlFilter : null;
        }));
    }

    public ReactiveEntityFilter<E> start() {
        reactiveQueryOptionalPush.start();
        refreshWhenActive();
        return this;
    }

    public ReactiveEntityFilter<E> stop() {
        reactiveQueryOptionalPush.stop();
        return this;
    }

    /*=========================
      === Other public API  ===
      =========================*/

    public final ReactiveEntityFilter<?> getParentFilter() {
        return parentFilter;
    }

    public final Object getDomainClassId() {
        return domainClassId;
    }

    public final DomainClass getDomainClass() {
        return getDomainModel().getClass(domainClassId);
    }

    @Override
    public final DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    @Override
    public final EntityStore getStore() {
        // If not set, we create a new store
        if (store == null)
            setStore(EntityStore.create(getDataSourceModel()));
        return store;
    }

    public ObservableValue<Boolean> activeProperty() {
        return reactiveQueryOptionalPush.activeProperty();
    }

    public ReactiveEntityFilter<E> setActive(boolean push) {
        reactiveQueryOptionalPush.setActive(push);
        return this;
    }

    public boolean isActive() {
        return reactiveQueryOptionalPush.isActive();
    }

    public final BooleanProperty pushProperty() {
        return reactiveQueryOptionalPush.pushProperty();
    }

    public final boolean isPush() {
        return reactiveQueryOptionalPush.isPush();
    }

    public ReactiveEntityFilter<E> setPush(boolean push) {
        reactiveQueryOptionalPush.setPush(push);
        return this;
    }

    public EntityList<E> getCurrentEntityList() {
        return getStore().getOrCreateEntityList(listId);
    }

    public boolean isStarted() {
        return reactiveQueryOptionalPush.isStarted();
    }

    public void refreshWhenActive() {
        reactiveQueryOptionalPush.refreshWhenReady(false);
    }


    /*===============================
      === Internal implementation ===
      ===============================*/

    private SqlCompiled sqlCompiled;

    protected final EntityList<E> emptyFutureList() {
        return EntityList.create(listId, getStore());
    }

    private EntityList<E> emptyCurrentList() {
        EntityList<E> list = getStore().getOrCreateEntityList(listId);
        list.clear();
        return list;
    }

    private void markEqlFiltersAsChanged() {
        eqlFiltersChangedSinceLastFetch = true;
        reactiveQueryOptionalPush.onArgumentChanged();
    }

    protected QueryArgument queryArgumentFetcher() {
        QueryArgument queryArgument = null;
        boolean active = isActive();
        boolean push = isPush();
        Object pushClientId = reactiveQueryOptionalPush.getReactiveQueryPush().getPushClientId();
        if (eqlFiltersChangedSinceLastFetch || hasPushInfoChanged(active, push, pushClientId)) {
            EqlFilter eqlFilter = mergeEqlFilters();
            // Shortcut: when the eql filter is "false", we return an empty entity list immediately (no server call) - unless we are in push mode already registered on the server
            if ((!push || lastPushClientId == null) && ("false".equals(eqlFilter.getWhere()) || "0".equals(eqlFilter.getLimit())))
                entityListProperty.set(emptyFutureList());
            else if (restrictedFilterList != null) {
                EntityList<E> filteredList = emptyCurrentList();
                filteredList.addAll(Entities.select(restrictedFilterList, eqlFilter.toStringSelect()));
                entityListProperty.set(filteredList);
            } else {
                // Otherwise we compile the final string filter into sql
                sqlCompiled = getDomainModel().parseAndCompileSelect(eqlFilter.toStringSelect());
                // And extract the possible parameters
                ArrayList<String> parameterNames = sqlCompiled.getParameterNames();
                Object[] parameterValues = Collections.isEmpty(parameterNames) ? null : Collections.map(parameterNames, name -> getStore().getParameterValue(name)).toArray();
                // Generating the query argument
                queryArgument = new QueryArgument(sqlCompiled.getSql(), parameterValues, getDataSourceId());
                // Skipping the server call if there is no difference in the parameters compared to the last call
                if (!isDifferentFromLastQuery(queryArgument, active, push, pushClientId)) {
                    queryArgument = null;
                    onSkippingQueryCallAsSameArgument();
                }
            }
            memorizeAsLastQuery(active, push, pushClientId);
            if (queryArgument != null)
                onBeforeQueryCall();
            eqlFiltersChangedSinceLastFetch = false;
        }
        return queryArgument;
    }

    protected void onSkippingQueryCallAsSameArgument() {
        Logger.log("No difference with previous query");
    }

    protected void onBeforeQueryCall() {
    }

    protected EqlFilter mergeEqlFilters() {
        EqlFilterBuilder mergeBuilder = new EqlFilterBuilder(getDomainClassId());
        for (ObservableValue<EqlFilter> eqlFilterProperty : eqlFilterProperties)
            mergeBuilder.merge(eqlFilterProperty.getValue());
        return mergeBuilder.build();
    }

    private boolean lastActive;
    private boolean lastPush;
    private Object lastPushClientId;

    private boolean hasPushInfoChanged(boolean active, boolean push, Object pushClientId) {
        return     push != lastPush
                || active != lastActive
                || !Objects.equals(pushClientId, lastPushClientId) && active;
    }

    private boolean isDifferentFromLastQuery(QueryArgument queryArgument, boolean active, boolean push, Object pushClientId) {
        return hasPushInfoChanged(active, push, pushClientId)
                || reactiveQueryOptionalPush.hasArgumentChangedSinceLastCall(queryArgument)
                ;
    }

    private void memorizeAsLastQuery(boolean active, boolean push, Object pushClientId) {
        lastActive = active;
        lastPush = push;
        lastPushClientId = pushClientId;
    }

    private void onNewQueryResult(QueryResult queryResult) {
        entityListProperty.set(queryResultToEntities(queryResult));
    }

    // Cache fields used in queryResultToEntities() method
    private QueryResult lastRsInput;
    private EntityList<E> lastEntitiesOutput;

    private EntityList<E> queryResultToEntities(QueryResult rs) {
        // Returning the cached output if input didn't change (ex: the same result set instance is emitted again on active property change)
        if (rs == lastRsInput)
            return lastEntitiesOutput;
        // Otherwise really generates the entity list (the content will changed but not the instance of the returned list)
        EntityList<E> entities = QueryResultToEntitiesMapper.mapQueryResultToEntities(rs, sqlCompiled.getQueryMapping(), getStore(), listId);
        // Caching and returning the result
        lastRsInput = rs;
        if (entities == lastEntitiesOutput) // It's also important to make sure the output instance is not the same
            entities = new EntityListWrapper<>(entities); // by wrapping the list (for entitiesToVisualResults() cache system)
        return lastEntitiesOutput = entities;
    }

    private boolean onEntityListChangedScheduled;

    protected void scheduleOnEntityListChanged() {
        if (!onEntityListChangedScheduled) {
            onEntityListChangedScheduled = true;
            Platform.runLater(() -> {
                onEntityListChangedScheduled = false;
                if (isStarted())
                    onEntityListChanged();
            });
        }
    }

    protected void onEntityListChanged() {
        /*if (!filterDisplays.isEmpty() && filterDisplays.get(0).visualResultProperty != null)
            applyVisualResults(entitiesToVisualResults(entityListProperty.get()));
        else*/ if (entitiesHandler != null)
            entitiesHandler.handle(entityListProperty.get());
    }

    protected void executeParsingCode(Runnable parsingCode) {
        ThreadLocalReferenceResolver.executeCodeInvolvingReferenceResolver(parsingCode, getRootAliasReferenceResolver());
    }

    public ReferenceResolver getRootAliasReferenceResolver() {
        if (rootAliasReferenceResolver == null) {
            // Before parsing, we prepare a ReferenceResolver to resolve possible references to root aliases
            Map<String, Alias<?>> rootAliases = new HashMap<>();
            rootAliasReferenceResolver = rootAliases::get;
            if (baseFilter != null) { // Root aliases are stored in the baseFilter
                // The first possible root alias is the base filter alias. Ex: Event e => the alias "e" then acts in a
                // similar way as "this" in java because it refers to the current Event row in the select, so some
                // expressions such as sub queries may refer to it (ex: select count(1) from Booking where event=e)
                String alias = baseFilter.getAlias();
                if (alias != null) // when defined, we add an Alias expression that can be returned when resolving this alias
                    rootAliases.put(alias, new Alias<>(alias, getDomainClass()));
                // Other possible root aliases can be As expressions defined in the base filter fields, such as sub queries
                // If fields contains for example (select ...) as xxx -> then xxx can be referenced in expression columns
                String fields = baseFilter.getFields();
                if (fields != null && fields.contains(" as ")) { // quick skipping if fields doesn't contains " as "
                    executeParsingCode(() -> {
                        for (Expression<?> field : getDomainModel().parseExpressionArray(fields, domainClassId).getExpressions()) {
                            if (field instanceof As) { // If a field is a As expression,
                                As<?> as = (As<?>) field;
                                // we add an Alias expression that can be returned when resolving this alias
                                rootAliases.put(as.getAlias(), new Alias<>(as.getAlias(), as.getType()));
                            }
                        }
                    });
                }
            }
        }
        return rootAliasReferenceResolver;
    }
}
