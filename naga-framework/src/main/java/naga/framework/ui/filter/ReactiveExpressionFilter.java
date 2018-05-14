package naga.framework.ui.filter;

import javafx.beans.property.*;
import javafx.beans.value.ObservableValue;
import naga.framework.activity.activeproperty.HasActiveProperty;
import naga.framework.expression.Expression;
import naga.framework.expression.builder.ReferenceResolver;
import naga.framework.expression.builder.ThreadLocalReferenceResolver;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.expression.terms.Alias;
import naga.framework.expression.terms.As;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.domainmodel.DomainModel;
import naga.framework.orm.entity.*;
import naga.framework.orm.mapping.QueryResultToEntityListGenerator;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.mapping.EntityListToDisplayResultGenerator;
import naga.framework.ui.rx.RxFuture;
import naga.framework.ui.rx.RxUi;
import naga.fx.properties.Properties;
import naga.fxdata.displaydata.DisplayColumnBuilder;
import naga.fxdata.displaydata.DisplayResult;
import naga.fxdata.displaydata.DisplaySelection;
import naga.platform.json.spi.JsonArray;
import naga.platform.json.spi.JsonObject;
import naga.platform.services.log.Logger;
import naga.platform.services.push.client.PushClientService;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResult;
import naga.platform.services.query.QueryService;
import naga.platform.services.query.push.QueryPushArgument;
import naga.platform.services.query.push.QueryPushService;
import naga.scheduler.Scheduler;
import naga.type.PrimType;
import naga.util.Booleans;
import naga.util.Numbers;
import naga.util.Strings;
import naga.util.async.Handler;
import naga.util.collection.Collections;
import naga.util.function.Callable;
import naga.util.function.Consumer;
import naga.util.function.Converter;
import naga.util.tuples.Unit;
import rx.Observable;
import rx.subjects.BehaviorSubject;

import java.util.*;

/**
 * @author Bruno Salmon
 */
public class ReactiveExpressionFilter<E extends Entity> implements HasActiveProperty, HasEntityStore {

    private final List<Observable<StringFilter>> stringFilterObservables = new ArrayList<>();
    private StringFilter lastStringFilter;
    private Object[] lastParameterValues;
    private boolean lastActive;
    private boolean lastPush;
    private Object lastPushClientId;
    private Observable<EntityList<E>> lastEntityListObservable;
    private final BehaviorSubject<StringFilter> lastStringFilterReEmitter = BehaviorSubject.create();
    private final BehaviorSubject<EntityList<E>> entityListQueryPushEmitter = BehaviorSubject.create();
    private List<E> restrictedFilterList;
    private DataSourceModel dataSourceModel;
    private I18n i18n;
    private EntityStore store;
    private static int filterCount = 0;
    private Object listId = "filter-" + ++filterCount;
    private boolean autoRefresh = false;
    private boolean requestRefreshOnActive = false;
    private boolean startsWithEmptyResult = true;
    private Object domainClassId;
    private StringFilter baseFilter;
    private Handler<EntityList<E>> entitiesHandler;
    private FilterDisplay filterDisplay;
    private List<FilterDisplay> filterDisplays = new ArrayList<>();
    private ReferenceResolver rootAliasReferenceResolver;
    private final BooleanProperty activeProperty = new SimpleBooleanProperty(true);
    private final BooleanProperty pushProperty = new SimpleBooleanProperty(false);
    private final ObjectProperty<Object> pushClientIdProperty = new SimpleObjectProperty<>();
    private Object queryStreamId;
    private ObservableValue<Boolean> boundActiveProperty;
    private boolean started;
    private boolean waitingQueryStreamId;
    private boolean queryHasChangeWhileWaitingQueryStreamId;

    public ReactiveExpressionFilter() {
        bindPushClientIdPropertyTo(PushClientService.pushClientIdProperty());
    }

    public ReactiveExpressionFilter(Object jsonOrClass) {
        this();
        combine(new StringFilterBuilder(jsonOrClass));
    }

    public Object getDomainClassId() {
        return domainClassId;
    }

    public DomainClass getDomainClass() {
        return getDomainModel().getClass(domainClassId);
    }

    public ReactiveExpressionFilter<E> setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        dataSourceModel.getDomainModel(); // Ensuring the data model is loaded with formats registered before expression columns are set
        return this;
    }

    public ReactiveExpressionFilter<E> setI18n(I18n i18n) {
        this.i18n = i18n;
        return this;
    }

    public ReactiveExpressionFilter<E> setStore(EntityStore store) {
        this.store = store;
        return this;
    }

    @Override
    public DataSourceModel getDataSourceModel() {
        return dataSourceModel;
    }

    @Override
    public EntityStore getStore() {
        // If not set, we create a new store
        if (store == null)
            setStore(EntityStore.create(getDataSourceModel()));
        return store;
    }

    public ReactiveExpressionFilter<E> setListId(Object listId) {
        this.listId = listId;
        return this;
    }

    public ReactiveExpressionFilter<E> setRestrictedFilterList(List<E> restrictedFilterList) {
        this.restrictedFilterList = restrictedFilterList;
        return this;
    }


    public ReactiveExpressionFilter<E> setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
        return this;
    }

    public ReactiveExpressionFilter<E> setStartsWithEmptyResult(boolean startsWithEmptyResult) {
        this.startsWithEmptyResult = startsWithEmptyResult;
        return this;
    }

    public ReactiveExpressionFilter<E> setEntitiesHandler(Handler<EntityList<E>> entitiesHandler) {
        this.entitiesHandler = entitiesHandler;
        return this;
    }

    public ObservableValue<Boolean> activeProperty() {
        return activeProperty;
    }

    public ReactiveExpressionFilter<E> setActive(boolean active) {
        activeProperty.setValue(active);
        return this;
    }

    public ReactiveExpressionFilter<E> bindActivePropertyTo(ObservableValue<Boolean> activeProperty) {
        if (activeProperty != null)
            this.activeProperty.bind(boundActiveProperty = activeProperty);
        return this;
    }

    public ObservableValue<Boolean> pushProperty() {
        return pushProperty;
    }

    public ReactiveExpressionFilter<E> setPush(boolean push) {
        pushProperty.setValue(push);
        return this;
    }

    public boolean isPush() {
        return pushProperty.getValue();
    }

    public ObservableValue pushClientIdProperty() {
        return pushClientIdProperty;
    }

    public ReactiveExpressionFilter<E> setPushClientId(Object pushClientId) {
        pushClientIdProperty.setValue(pushClientId);
        return this;
    }

    public Object getPushClientId() {
        return pushClientIdProperty.getValue();
    }

    public ReactiveExpressionFilter<E> bindPushClientIdPropertyTo(ObservableValue pushClientIdProperty) {
        if (pushClientIdProperty != null)
            this.pushClientIdProperty.bind(pushClientIdProperty);
        return this;
    }

    public ReactiveExpressionFilter<E> combine(String json) {
        return combine(new StringFilter(json));
    }

    public ReactiveExpressionFilter<E> combine(JsonObject json) {
        return combine(new StringFilter(json));
    }

    public ReactiveExpressionFilter<E> combine(StringFilterBuilder stringFilterBuilder) {
        return combine(stringFilterBuilder.build());
    }

    public ReactiveExpressionFilter<E> combine(StringFilter stringFilter) {
        if (domainClassId == null) {
            domainClassId = stringFilter.getDomainClassId();
            baseFilter = stringFilter;
        }
        return combine(Observable.just(stringFilter));
    }

    public ReactiveExpressionFilter<E> combine(Observable<StringFilter> stringFilterObservable) {
        stringFilterObservables.add(stringFilterObservable);
        return this;
    }

    public <T> ReactiveExpressionFilter<E> combine(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return combine(RxUi.observe(property)
                .map(t -> {
                    String json = toJsonFilterConverter.convert(t);
                    return json == null ? null : new StringFilter(json);
                }));
    }

    public <T> ReactiveExpressionFilter<E> combineIfNotNull(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return combine(property, value -> value == null ? null : toJsonFilterConverter.convert(value));
    }

    public ReactiveExpressionFilter<E> combineIfNotEmpty(ObservableValue<String> property, Converter<String, String> toJsonFilterConverter) {
        return combine(property, s -> Strings.isEmpty(s) ? null : toJsonFilterConverter.convert(s));
    }

    public ReactiveExpressionFilter<E> combineTrimIfNotEmpty(ObservableValue<String> property, Converter<String, String> toJsonFilterConverter) {
        return combineIfNotEmpty(property, s -> toJsonFilterConverter.convert(Strings.trim(s)));
    }

    public <T extends Number> ReactiveExpressionFilter<E> combineIfPositive(ObservableValue<T> property, Converter<T, String> toJsonFilterConverter) {
        return combine(property, value -> Numbers.isPositive(value) ? toJsonFilterConverter.convert(value) : null);
    }

    public ReactiveExpressionFilter<E> combineIfTrue(ObservableValue<Boolean> property, Callable<String> toJsonFilterConverter) {
        return combine(property, value -> Booleans.isTrue(value) ? toJsonFilterConverter.call() : null);
    }

    public ReactiveExpressionFilter<E> combine(Property<Boolean> ifProperty, StringFilterBuilder stringFilterBuilder) {
        return combine(ifProperty, stringFilterBuilder.build());
    }

    public ReactiveExpressionFilter<E> combine(Property<Boolean> ifProperty, String json) {
        return combine(ifProperty, new StringFilter(json));
    }

    public ReactiveExpressionFilter<E> combine(Property<Boolean> ifProperty, StringFilter stringFilter) {
        return combine(RxUi.observeIf(Observable.just(stringFilter), ifProperty));
    }

    public ReactiveExpressionFilter<E> nextDisplay() {
        goToNextFilterDisplayIfDisplayResultPropertyIsSet();
        return this;
    }

    private void goToNextFilterDisplayIfDisplayResultPropertyIsSet() {
        if (filterDisplay != null && filterDisplay.displayResultProperty != null) {
            filterDisplay.stringFilterObservableLastIndex = stringFilterObservables.size() - 1;
            filterDisplay = null;
        }
    }

    private FilterDisplay getFilterDisplay() {
        if (started)
            filterDisplay = filterDisplays.get(0);
        else if (filterDisplay == null)
            filterDisplays.add(filterDisplay = new FilterDisplay());
        return filterDisplay;
    }

    public ReactiveExpressionFilter<E> setDisplaySelectionProperty(Property<DisplaySelection> displaySelectionProperty) {
        getFilterDisplay().setDisplaySelectionProperty(displaySelectionProperty);
        return this;
    }

    public ReactiveExpressionFilter<E> setSelectedEntityHandler(Handler<E> entityHandler) {
        getFilterDisplay().setSelectedEntityHandler(entityHandler);
        return this;
    }

    public ReactiveExpressionFilter<E> setSelectedEntityHandler(Property<DisplaySelection> displaySelectionProperty, Handler<E> entityHandler) {
        getFilterDisplay().setSelectedEntityHandler(displaySelectionProperty, entityHandler);
        return this;
    }

    public ReactiveExpressionFilter<E> selectFirstRowOnFirstDisplay() {
        getFilterDisplay().selectFirstRowOnFirstDisplay();
        return this;
    }

    public ReactiveExpressionFilter<E> selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty, Property onEachChangeProperty) {
        getFilterDisplay().selectFirstRowOnFirstDisplay(displaySelectionProperty, onEachChangeProperty);
        return this;
    }

    public ReactiveExpressionFilter<E> selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty) {
        getFilterDisplay().selectFirstRowOnFirstDisplay(displaySelectionProperty);
        return this;
    }

    public ReactiveExpressionFilter<E> setExpressionColumns(String jsonArrayDisplayColumns) {
        getFilterDisplay().setExpressionColumns(jsonArrayDisplayColumns);
        return this;
    }

    public ReactiveExpressionFilter<E> setExpressionColumns(JsonArray array) {
        getFilterDisplay().setExpressionColumns(array);
        return this;
    }

    public ReactiveExpressionFilter<E> setExpressionColumns(ExpressionColumn... expressionColumns) {
        getFilterDisplay().setExpressionColumns(expressionColumns);
        return this;
    }

    public ReactiveExpressionFilter<E> applyDomainModelRowStyle() {
        getFilterDisplay().applyDomainModelRowStyle();
        return this;
    }

    public ReactiveExpressionFilter<E> displayResultInto(Property<DisplayResult> displayResultProperty) {
        getFilterDisplay().setDisplayResultProperty(displayResultProperty);
        return this;
    }


    public Property<DisplaySelection> getDisplaySelectionProperty() {
        return getDisplaySelectionProperty(0);
    }

    public Property<DisplaySelection> getDisplaySelectionProperty(int displayIndex) {
        return filterDisplays.get(displayIndex).getDisplaySelectionProperty();
    }

    public E getSelectedEntity() {
        return getSelectedEntity(0);
    }

    public E getSelectedEntity(int displayIndex) {
        return filterDisplays.get(displayIndex).getSelectedEntity();
    }

    public E getSelectedEntity(DisplaySelection selection) {
        return getSelectedEntity(0, selection);
    }

    public E getSelectedEntity(int displayIndex, DisplaySelection selection) {
        return filterDisplays.get(displayIndex).getSelectedEntity(selection);
    }

    public EntityList<E> getCurrentEntityList() {
        return getCurrentEntityList(0);
    }

    public EntityList<E> getCurrentEntityList(int displayIndex) {
        return filterDisplays.get(displayIndex).getCurrentEntityList();
    }

    public ReactiveExpressionFilter<E> start() {
        combine(activeProperty, "{}");
        combine(pushProperty, "{}");
        combine(pushClientIdProperty, p -> "{}");
        // if autoRefresh is set, we combine the filter with a 5s tic tac property
        if (autoRefresh) {
            Property<Boolean> ticTacProperty = new SimpleObjectProperty<>(true);
            Scheduler.schedulePeriodic(5000, () -> {
                if (isActive())
                    ticTacProperty.setValue(!ticTacProperty.getValue());
            });
            combine(ticTacProperty, "{}");
        }
        // The following call is to set stringFilterObservableLastIndex on the latest filterDisplay
        goToNextFilterDisplayIfDisplayResultPropertyIsSet();
        // Initializing the display with empty results (no rows but columns) so the component (probably a table) display the columns before calling the server
        if (startsWithEmptyResult)
            resetAllDisplayResults(true);
        // Also adding a listener reacting to a language change by updating the columns translations immediately (without making a new server request)
        if (i18n != null)
            Properties.runOnPropertiesChange(new Consumer<ObservableValue>() {
                private boolean dictionaryChanged;

                @Override
                public void accept(ObservableValue p) {
                    dictionaryChanged |= p == i18n.dictionaryProperty();
                    if (dictionaryChanged) {
                        lastEntitiesInput = null; // Clearing the cache to have a fresh display result set next time it is active
                        if (isActive()) {
                            resetAllDisplayResults(false);
                            dictionaryChanged = false;
                        }
                    } else if (requestRefreshOnActive && isActive())
                        refreshNow();
                }
            }, i18n.dictionaryProperty(), activeProperty);
        Unit<QueryArgument> queryArgumentHolder = new Unit<>(); // Used for skipping possible too old query results
        Unit<QueryResult> queryResultHolder = new Unit<>(); // Used for
        Observable<EntityList<E>> entityListObservable = Observable
                .combineLatest(stringFilterObservables, this::mergeStringFilters)
                .mergeWith(lastStringFilterReEmitter)
                .switchMap(stringFilter -> {
                    boolean active = isActive();
                    boolean push = isPush();
                    Object pushClientId = getPushClientId();
                    Object[] parameterValues = null;
                    // Shortcut: when the string filter is "false", we return an empty entity list immediately (no server call)
                    if ("false".equals(stringFilter.getWhere()) || "0".equals(stringFilter.getLimit()))
                        lastEntityListObservable = Observable.just(emptyFutureList());
                    else if (restrictedFilterList != null) {
                        EntityList<E> filteredList = emptyCurrentList();
                        filteredList.addAll(Entities.select(restrictedFilterList, stringFilter.toStringSelect()));
                        lastEntityListObservable = Observable.just(filteredList);
                    } else {
                        // Otherwise we compile the final string filter into sql
                        SqlCompiled sqlCompiled = getDomainModel().compileSelect(stringFilter.toStringSelect());
                        // And extract the possible parameters
                        ArrayList<String> parameterNames = sqlCompiled.getParameterNames();
                        parameterValues = Collections.isEmpty(parameterNames) ? null : Collections.map(parameterNames, name -> getStore().getParameterValue(name)).toArray(); // Doesn't work on Android: parameterNames.stream().map(name -> getStore().getParameterValue(name)).toArray();
                        // Skipping the server call if there is no difference in the parameters compared to the last call (unless it is in autoRefresh mode)
                        if (autoRefresh || isDifferentFromLastQuery(stringFilter, parameterValues, active, push, pushClientId)) {
                            // Generating the query argument
                            QueryArgument queryArgument = new QueryArgument(sqlCompiled.getSql(), parameterValues, getDataSourceId());
                            if (!push) { // Standard mode (and not push mode) with only 1 result expected from the server
                                // Holding the query argument passed to the server for double check when receiving the result
                                queryArgumentHolder.set(queryArgument);
                                // Calling the query service (probably on server)
                                lastEntityListObservable = RxFuture.from(QueryService.executeQuery(queryArgument))
                                    .map(queryResult ->
                                        // Double checking the query argument is still the latest and if ok, transforming the QueryResult into entities
                                        queryArgument.equals(queryArgumentHolder.get()) ? queryResultToEntities(queryResult, sqlCompiled)
                                        // If not ok, this means the result is too old (another newer request has been sent meanwhile)
                                        : null); // se we just return null in this case (will be ignored)
                            } else /* push mode -> possible multiple results pushed by the server */ if (pushClientId != null) { // pushClientId is null when the client is not yet connected to the server (so waiting the pushClientId before calling the server)
                                lastEntityListObservable = entityListQueryPushEmitter;
                                if (queryStreamId != null || active) { // Skipping new stream not yet active (waiting it becomes active before calling the server)
                                    QueryArgument lastQueryArgument = queryArgumentHolder.get();
                                    if (waitingQueryStreamId) // If we already wait the queryStreamId, we won't make a new call now (we can't update the stream without its id)
                                        queryHasChangeWhileWaitingQueryStreamId |= !queryArgument.equals(lastQueryArgument); // but we mark this flag in order to update the stream (if modified) when receiving its id
                                    else { // All conditions are gathered (different query, client connected and no pending call) to make a query push server call
                                        // Network optimization: not necessary to send the query argument again if it has already been sent and hasn't change (the server kept a copy of it)
                                        QueryArgument transmittedQueryArgument = queryStreamId != null && queryArgument.equals(lastQueryArgument) ? null : queryArgument;
                                        waitingQueryStreamId = queryStreamId == null; // Setting the waitingQueryStreamId flag to true when queryStreamId is not yet known
                                        queryArgumentHolder.set(queryArgument); // Holding the query argument passed to the server for double check when receiving the result
                                        Logger.log("Calling query push: queryStreamId=" + queryStreamId + ", pushClientId=" + pushClientId + ", active=" + active + ", queryArgument=" + transmittedQueryArgument);
                                        QueryPushService.executeQueryPush(new QueryPushArgument(queryStreamId, pushClientId, transmittedQueryArgument, queryPushResult -> { // This consumer is called for each result pushed by the server
                                            // Double checking the query argument is still the latest and if ok, transforming the QueryResult into entities
                                            if (queryArgument.equals(queryArgumentHolder.get())) {
                                                QueryResult queryResult = queryPushResult.getQueryResult();
                                                if (queryResult == null)
                                                    queryResult = queryPushResult.getQueryResultDiff().applyTo(queryResultHolder.get());
                                                queryResultHolder.set(queryResult);
                                                entityListQueryPushEmitter.onNext(queryResultToEntities(queryResult, sqlCompiled));
                                            }
                                        }, getDataSourceId(), active, null)).setHandler(ar -> { // This handler is called only once when the query push service call returns
                                            queryStreamId = ar.result(); // the result is the queryStreamId returned by server (or null if failed)
                                            waitingQueryStreamId = false; // Resetting the waitingQueryStreamId flag to false
                                            // Cases where we need to trigger a new query push service call:
                                            if (ar.failed() // 1) on failure (this may happen if queryStreamId is not registered on the server anymore, for ex after server restart with a non persistent query push provider such as the in-memory default one)
                                                    || queryHasChangeWhileWaitingQueryStreamId) { // 2) when the query has changed while we were waiting for the query stream id
                                                Logger.log((active ? "Refreshing filter " + listId : "Filter " + listId + " will be refreshed when active") + (queryHasChangeWhileWaitingQueryStreamId ? " because the query has changed while waiting the queryStreamId" : " because a failure occurred while updating the query (may be an unrecognized queryStreamId after server restart)"));
                                                queryHasChangeWhileWaitingQueryStreamId = false; // Resetting the flag
                                                refreshWhenActive(); // This will trigger an new pass (when active) leading to a new call to the query push service
                                            }
                                        });
                                    }
                                }
                            }
                        }
                    }
                    memorizeAsLastQuery(stringFilter, parameterValues, active, push, pushClientId);
                    return lastEntityListObservable;
                });
        if (!filterDisplays.isEmpty() && filterDisplays.get(0).displayResultProperty != null)
            entityListObservable
                    .map(this::entitiesToDisplayResults) // Finally transforming the EntityList into a DisplayResult also calls entitiesHandler
                    .subscribe(this::applyDisplayResults);
        else if (entitiesHandler != null)
            entityListObservable.subscribe(entitiesHandler::handle);
        started = true;
        return this;
    }

    public boolean isStarted() {
        return started;
    }

    private boolean isDifferentFromLastQuery(StringFilter stringFilter, Object[] parameterValues, boolean active, boolean push, Object pushClientId) {
        return !Objects.equals(stringFilter, lastStringFilter)
                || !Arrays.equals(parameterValues, lastParameterValues)
                || push != lastPush
                || active != lastActive
                || !Objects.equals(pushClientId, lastPushClientId) && active
                ;
    }

    private void memorizeAsLastQuery(StringFilter stringFilter, Object[] parameterValues, boolean active, boolean push, Object pushClientId) {
        lastStringFilter = stringFilter;
        lastParameterValues = parameterValues;
        lastActive = active;
        lastPush = push;
        lastPushClientId = pushClientId;
    }

    private EntityList<E> emptyFutureList() {
        return EntityList.create(listId, getStore());
    }

    private EntityList<E> emptyCurrentList() {
        EntityList<E> list = getStore().getOrCreateEntityList(listId);
        list.clear();
        return list;
    }

    private void refreshNowIfActiveAndFilterChanged() {
        if (started && isActive()) {
            activeProperty.unbind();
            setActive(false);
            setActive(true);
            if (boundActiveProperty != null)
                bindActivePropertyTo(boundActiveProperty);
        }
    }

    public void refreshWhenActive() {
        if (isActive())
            refreshNow();
        else
            requestRefreshOnActive = true;
    }

    private void refreshNow() {
        StringFilter stringFilter = lastStringFilter;
        if (stringFilter != null) {
            lastStringFilter = null;
            lastStringFilterReEmitter.onNext(stringFilter);
        }
        requestRefreshOnActive = false;
    }

    private StringFilter mergeStringFilters(Object... args) {
        Iterator<FilterDisplay> it = filterDisplays.iterator();
        filterDisplay = Collections.next(it);
        StringFilterBuilder mergeBuilder = new StringFilterBuilder(domainClassId);
        for (int i = 0; i < args.length; i++) {
            mergeBuilder.merge((StringFilter) args[i]);
            if (filterDisplay != null && filterDisplay.stringFilterObservableLastIndex == i) {
                if (mergeBuilder.getColumns() != null) {
                    filterDisplay.setExpressionColumnsPrivate(mergeBuilder.getColumns());
                    mergeBuilder.setColumns(null);
                }
                filterDisplay = Collections.next(it);
            }
        }
        // If expression columns have persistent fields, we include them in the fields to load
        for (FilterDisplay filterDisplay : filterDisplays) {
            List<Expression> columnsPersistentTerms = filterDisplay.collectColumnsPersistentTerms();
            if (!columnsPersistentTerms.isEmpty())
                mergeBuilder.mergeFields(new ExpressionArray<>(columnsPersistentTerms).toString());
        }
        return mergeBuilder.build();
    }

    private void resetAllDisplayResults(boolean empty) {
        for (FilterDisplay filterDisplay : filterDisplays)
            filterDisplay.resetDisplayResult(empty);
    }

    // Cache fields used in queryResultToEntities() method
    private QueryResult lastRsInput;
    private EntityList lastEntitiesOutput;

    private EntityList queryResultToEntities(QueryResult rs, SqlCompiled sqlCompiled) {
        // Returning the cached output if input didn't change (ex: the same result set instance is emitted again on active property change)
        if (rs == lastRsInput)
            return lastEntitiesOutput;
        // Otherwise really generates the entity list (the content will changed but not the instance of the returned list)
        EntityList<E> entities = QueryResultToEntityListGenerator.createEntityList(rs, sqlCompiled.getQueryMapping(), getStore(), listId);
        // Caching and returning the result
        lastRsInput = rs;
        if (entities == lastEntitiesOutput) // It's also important to make sure the output instance is not the same
            entities = new EntityListWrapper<>(entities); // by wrapping the list (for entitiesToDisplayResults() cache system)
        return lastEntitiesOutput = entities;
    }

    // Cache fields used in entitiesToDisplayResults() method
    private EntityList lastEntitiesInput;
    private DisplayResult[] lastDisplayResultsOutput;

    private DisplayResult[] entitiesToDisplayResults(EntityList<E> entities) {
        // Returning the cached output if input didn't change (ex: the same entity list instance is emitted again on active property change)
        if (entities == lastEntitiesInput)
            return lastDisplayResultsOutput; // Returning the same instance will avoid triggering the results changeListeners (high cpu consuming in UI)
        // Calling the entities handler now we are sure there is a real change
        if (entitiesHandler != null)
            entitiesHandler.handle(entities);
        // Transforming the entities into displayResults (entity to display mapping)
        int n = filterDisplays.size();
        DisplayResult[] results = new DisplayResult[n];
        for (int i = 0; i < n; i++)
            results[i] = filterDisplays.get(i).entitiesListToDisplayResult(entities);
        // Caching and returning the result
        lastEntitiesInput = entities;
        return lastDisplayResultsOutput = results;
    }

    private void applyDisplayResults(DisplayResult[] displayResults) {
        for (int i = 0, n = displayResults.length; i < n; i++)
            filterDisplays.get(i).setDisplayResult(displayResults[i]);
    }

    private ReferenceResolver getRootAliasReferenceResolver() {
        if (rootAliasReferenceResolver == null) {
            // Before parsing, we prepare a ReferenceResolver to resolve possible references to root aliases
            Map<String, Alias> rootAliases = new HashMap<>();
            rootAliasReferenceResolver = rootAliases::get;
            if (baseFilter != null) { // Root aliases are stored in the baseFilter
                // The first possible root alias is the base filter alias. Ex: Event e => the alias "e" then acts in a
                // similar way as "this" in java because it refers to the current Event row in the select, so some
                // expressions such as sub queries may refer to it (ex: select count(1) from Booking where event=e)
                String alias = baseFilter.getAlias();
                if (alias != null) // when defined, we add an Alias expression that can be returned when resolving this alias
                    rootAliases.put(alias, new Alias(alias, getDomainClass()));
                // Other possible root aliases can be As expressions defined in the base filter fields, such as sub queries
                // If fields contains for example (select ...) as xxx -> then xxx can be referenced in expression columns
                String fields = baseFilter.getFields();
                if (fields != null && fields.contains(" as ")) { // quick skipping if fields doesn't contains " as "
                    try {
                        ThreadLocalReferenceResolver.pushReferenceResolver(rootAliasReferenceResolver);
                        // Now that the ReferenceResolver is ready, we can parse the expression columns
                        for (Expression field : getDomainModel().parseExpressionArray(fields, domainClassId).getExpressions()) {
                            if (field instanceof As) { // If a field is a As expression,
                                As as = (As) field;
                                // we add an Alias expression that can be returned when resolving this alias
                                rootAliases.put(as.getAlias(), new Alias(as.getAlias(), as.getType()));
                            }
                        }
                    } finally {
                        ThreadLocalReferenceResolver.popReferenceResolver();
                    }
                }
            }
        }
        return rootAliasReferenceResolver;
    }

    private class FilterDisplay {
        ExpressionColumn[] expressionColumns;
        Property<DisplayResult> displayResultProperty;
        Property<DisplaySelection> displaySelectionProperty;
        boolean selectFirstRowOnFirstDisplay;
        int stringFilterObservableLastIndex = -1;
        List<Expression> columnsPersistentTerms;
        boolean appliedDomainModelRowStyle;

        Property<DisplaySelection> getDisplaySelectionProperty() {
            return displaySelectionProperty;
        }

        void setDisplaySelectionProperty(Property<DisplaySelection> displaySelectionProperty) {
            this.displaySelectionProperty = displaySelectionProperty;
        }

        Property<DisplayResult> getDisplayResultProperty() {
            return displayResultProperty;
        }

        void setSelectedEntityHandler(Handler<E> entityHandler) {
            displaySelectionProperty.addListener((observable, oldValue, newValue) -> entityHandler.handle(getSelectedEntity()));
        }

        void setSelectedEntityHandler(Property<DisplaySelection> displaySelectionProperty, Handler<E> entityHandler) {
            setDisplaySelectionProperty(displaySelectionProperty);
            setSelectedEntityHandler(entityHandler);
        }

        void selectFirstRowOnFirstDisplay() {
            selectFirstRowOnFirstDisplay = true;
        }

        void selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty, Property onEachChangeProperty) {
            // Each time the property change, we clear the selection and reset the selectFirstRowOnFirstDisplay to true to arm the mechanism again
            Properties.runOnPropertiesChange(() -> {
                if (isActive()) {
                    displaySelectionProperty.setValue(null);
                    selectFirstRowOnFirstDisplay();
                }
            }, onEachChangeProperty, activeProperty);
            selectFirstRowOnFirstDisplay(displaySelectionProperty);
        }

        void selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty) {
            setDisplaySelectionProperty(displaySelectionProperty);
            selectFirstRowOnFirstDisplay();
        }

        E getSelectedEntity() {
            return getSelectedEntity(displaySelectionProperty.getValue());
        }

        E getSelectedEntity(DisplaySelection selection) {
            E selectedEntity = null;
            int selectedRow = selection == null ? -1 : selection.getSelectedRow();
            if (selectedRow >= 0)
                selectedEntity = getCurrentEntityList().get(selectedRow);
            return selectedEntity;
        }

        EntityList<E> getCurrentEntityList() {
            return getStore().getOrCreateEntityList(listId);
        }

        void setExpressionColumns(String jsonArrayDisplayColumns) {
            setExpressionColumns(ExpressionColumn.fromJsonArray(jsonArrayDisplayColumns));
        }

        void setExpressionColumns(JsonArray array) {
            setExpressionColumns(ExpressionColumn.fromJsonArray(array));
        }

        void setExpressionColumns(ExpressionColumn... expressionColumns) {
            setExpressionColumnsPrivate(expressionColumns);
            if (appliedDomainModelRowStyle)
                applyDomainModelRowStyle();
            refreshNowIfActiveAndFilterChanged();
        }

        void setExpressionColumnsPrivate(String jsonArrayDisplayColumns) {
            setExpressionColumnsPrivate(ExpressionColumn.fromJsonArray(jsonArrayDisplayColumns));
            if (appliedDomainModelRowStyle)
                applyDomainModelRowStyle();
        }

        void setExpressionColumnsPrivate(ExpressionColumn... expressionColumns) {
            this.expressionColumns = expressionColumns;
            columnsPersistentTerms = null; // forcing re-computation on next collectColumnsPersistentTerms() call since the columns have changed
        }

        void applyDomainModelRowStyle() {
            DomainClass domainClass = getDomainClass();
            ExpressionArray rowStylesExpressionArray = domainClass.getStyleClassesExpressionArray();
            if (rowStylesExpressionArray != null && expressionColumns != null) {
                ExpressionColumn[] includingRowStyleColumns = new ExpressionColumn[expressionColumns.length + 1];
                includingRowStyleColumns[0] = ExpressionColumn.create(rowStylesExpressionArray, DisplayColumnBuilder.create("style", PrimType.STRING).setRole("style").build());
                System.arraycopy(expressionColumns, 0, includingRowStyleColumns, 1, expressionColumns.length);
                setExpressionColumnsPrivate(includingRowStyleColumns);
            }
            appliedDomainModelRowStyle = true;
        }

        void setDisplayResultProperty(Property<DisplayResult> displayResultProperty) {
            this.displayResultProperty = displayResultProperty;
        }

        void setDisplayResult(DisplayResult rs) {
            displayResultProperty.setValue(rs);
            if (selectFirstRowOnFirstDisplay && rs.getRowCount() > 0) {
                selectFirstRowOnFirstDisplay = false;
                displaySelectionProperty.setValue(DisplaySelection.createSingleRowSelection(0));
            }
        }

        void resetDisplayResult(boolean empty) {
            if (empty)
                setEmptyDisplayResult();
            else
                displayResultProperty.setValue(entitiesListToDisplayResult(getCurrentEntityList()));
        }

        void setEmptyDisplayResult() {
            displayResultProperty.setValue(emptyDisplayResult());
        }

        DisplayResult entitiesListToDisplayResult(List<E> entities) {
            collectColumnsPersistentTerms();
            return EntityListToDisplayResultGenerator.createDisplayResult(entities, expressionColumns, i18n);
        }

        DisplayResult emptyDisplayResult() {
            return entitiesListToDisplayResult(emptyFutureList());
        }

        @SuppressWarnings("unchecked")
        List<Expression> collectColumnsPersistentTerms() {
            if (columnsPersistentTerms == null) {
                columnsPersistentTerms = new ArrayList<>();
                if (expressionColumns != null)
                    try {
                        DomainModel domainModel = getDomainModel();
                        ThreadLocalReferenceResolver.pushReferenceResolver(getRootAliasReferenceResolver());
                        // Now that the ReferenceResolver is ready, we can parse the expression columns
                        for (ExpressionColumn expressionColumn : expressionColumns) {
                            expressionColumn.parseExpressionDefinitionIfNecessary(domainModel, domainClassId);
                            expressionColumn.getExpression().collectPersistentTerms(columnsPersistentTerms);
                        }
                    } finally {
                        ThreadLocalReferenceResolver.popReferenceResolver();
                    }
            }
            return columnsPersistentTerms;
        }
    }
}