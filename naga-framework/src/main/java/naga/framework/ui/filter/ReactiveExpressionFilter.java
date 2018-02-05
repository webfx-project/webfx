package naga.framework.ui.filter;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
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
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.mapping.EntityListToDisplayResultSetGenerator;
import naga.framework.ui.rx.RxFuture;
import naga.framework.ui.rx.RxUi;
import naga.fx.properties.Properties;
import naga.fxdata.displaydata.DisplayColumnBuilder;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.fxdata.displaydata.DisplaySelection;
import naga.platform.json.spi.JsonArray;
import naga.platform.json.spi.JsonObject;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.services.query.spi.QueryService;
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
import rx.Observable;
import rx.subjects.BehaviorSubject;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bruno Salmon
 */
public class ReactiveExpressionFilter<E extends Entity> implements HasActiveProperty, HasEntityStore {

    private final List<Observable<StringFilter>> stringFilterObservables = new ArrayList<>();
    private StringFilter lastStringFilter;
    private Object[] lastParameterValues;
    private Observable<EntityList<E>> lastEntityListObservable;
    private final BehaviorSubject<StringFilter> lastStringFilterReEmitter = BehaviorSubject.create();
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
    private ObservableValue<Boolean> boundActiveProperty;
    private boolean started;

    public ReactiveExpressionFilter() {
    }

    public Object getDomainClassId() {
        return domainClassId;
    }

    public DomainClass getDomainClass() {
        return getDomainModel().getClass(domainClassId);
    }

    public ReactiveExpressionFilter(Object jsonOrClass) {
        combine(new StringFilterBuilder(jsonOrClass));
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
        goToNextFilterDisplayIfDisplayResultSetPropertyIsSet();
        return this;
    }

    private void goToNextFilterDisplayIfDisplayResultSetPropertyIsSet() {
        if (filterDisplay != null && filterDisplay.displayResultSetProperty != null) {
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

    public ReactiveExpressionFilter<E> displayResultSetInto(Property<DisplayResultSet> displayResultSetProperty) {
        getFilterDisplay().setDisplayResultSetProperty(displayResultSetProperty);
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
        // if autoRefresh is set, we combine the filter with a 5s tic tac property
        if (autoRefresh) {
            Property<Boolean> ticTacProperty = new SimpleObjectProperty<>(true);
            Runnable runnable = () -> {if (isActive()) ticTacProperty.setValue(!ticTacProperty.getValue());};
            Scheduler.schedulePeriodic(5000, runnable);
            combine(ticTacProperty, "{}");
        }
        // The following call is to set stringFilterObservableLastIndex on the latest filterDisplay
        goToNextFilterDisplayIfDisplayResultSetPropertyIsSet();
        // Initializing the display with empty results (no rows but columns) so the component (probably a table) display the columns before calling the server
        if (startsWithEmptyResult)
            resetAllDisplayResultSets(true);
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
                            resetAllDisplayResultSets(false);
                            dictionaryChanged = false;
                        }
                    } else if (requestRefreshOnActive && isActive())
                        refreshNow();
                }
            }, i18n.dictionaryProperty(), activeProperty);
        AtomicInteger querySequence = new AtomicInteger(); // Used for skipping possible too old query results
        Observable<StringFilter> resultingStringFilterObservable = Observable
                .combineLatest(stringFilterObservables, this::mergeStringFilters);
        resultingStringFilterObservable = resultingStringFilterObservable.mergeWith(lastStringFilterReEmitter);
        Observable<EntityList<E>> entityListObservable = resultingStringFilterObservable.switchMap(stringFilter -> {
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
                ArrayList<String> parameterNames = sqlCompiled.getParameterNames();
                parameterValues = Collections.isEmpty(parameterNames) ? null : Collections.map(parameterNames, name -> getStore().getParameterValue(name)).toArray(); // Doesn't work on Android: parameterNames.stream().map(name -> getStore().getParameterValue(name)).toArray();
                if (autoRefresh || isDifferentFromLastQuery(stringFilter, parameterValues)) {
                    // We increment and capture the sequence to check if the request is still the latest one when receiving the result
                    int sequence = querySequence.incrementAndGet();
                    // Then we ask the query service to execute the sql query
                    lastEntityListObservable = RxFuture.from(QueryService.executeQuery(new QueryArgument(sqlCompiled.getSql(), parameterValues, getDataSourceModel().getId())))
                            // Aborting the process (returning null) if the sequence differs (meaning a new request has been sent)
                            // Otherwise transforming the QueryResultSet into an EntityList
                            .map(queryResultSet -> (sequence != querySequence.get()) ? null : queryResultSetToEntities(queryResultSet, sqlCompiled));
                }
            }
            memorizeAsLastQuery(stringFilter, parameterValues);
            return lastEntityListObservable;
        });
        if (!filterDisplays.isEmpty() && filterDisplays.get(0).displayResultSetProperty != null)
            entityListObservable
                // Finally transforming the EntityList into a DisplayResultSet
                .map(this::entitiesToDisplayResultSets) // also calls entitiesHandler
                .subscribe(this::applyDisplayResultSets);
        else if (entitiesHandler != null)
            entityListObservable.subscribe(entitiesHandler::handle);
        started = true;
        return this;
    }

    public boolean isStarted() {
        return started;
    }

    private boolean isDifferentFromLastQuery(StringFilter stringFilter, Object[] parameterValues) {
        return !Objects.equals(stringFilter, lastStringFilter) || !Arrays.equals(parameterValues, lastParameterValues);
    }

    private void memorizeAsLastQuery(StringFilter stringFilter, Object[] parameterValues) {
        lastStringFilter = stringFilter;
        lastParameterValues = parameterValues;
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
        StringFilter stringFilter = this.lastStringFilter;
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

    private void resetAllDisplayResultSets(boolean empty) {
        for (FilterDisplay filterDisplay : filterDisplays)
            filterDisplay.resetDisplayResultSet(empty);
    }

    // Cache fields used in queryResultSetToEntities() method
    private QueryResultSet lastRsInput;
    private EntityList lastEntitiesOutput;

    private EntityList queryResultSetToEntities(QueryResultSet rs, SqlCompiled sqlCompiled) {
        // Returning the cached output if input didn't change (ex: the same result set instance is emitted again on active property change)
        if (rs == lastRsInput)
            return lastEntitiesOutput;
        // Otherwise really generates the entity list (the content will changed but not the instance of the returned list)
        EntityList<E> entities = QueryResultSetToEntityListGenerator.createEntityList(rs, sqlCompiled.getQueryMapping(), getStore(), listId);
        // Caching and returning the result
        lastRsInput = rs;
        if (entities == lastEntitiesOutput) // It's also important to make sure the output instance is not the same
            entities = new EntityListWrapper<>(entities); // by wrapping the list (for entitiesToDisplayResultSets() cache system)
        return lastEntitiesOutput = entities;
    }

    // Cache fields used in entitiesToDisplayResultSets() method
    private EntityList lastEntitiesInput;
    private DisplayResultSet[] lastDisplayResultSetsOutput;

    private DisplayResultSet[] entitiesToDisplayResultSets(EntityList<E> entities) {
        // Returning the cached output if input didn't change (ex: the same entity list instance is emitted again on active property change)
        if (entities == lastEntitiesInput)
            return lastDisplayResultSetsOutput; // Returning the same instance will avoid triggering the resultSets changeListeners (high cpu consuming in UI)
        // Calling the entities handler now we are sure there is a real change
        if (entitiesHandler != null)
            entitiesHandler.handle(entities);
        // Transforming the entities into displayResultSets (entity to display mapping)
        int n = filterDisplays.size();
        DisplayResultSet[] resultSets = new DisplayResultSet[n];
        for (int i = 0; i < n; i++)
            resultSets[i] = filterDisplays.get(i).entitiesListToDisplayResultSet(entities);
        // Caching and returning the result
        lastEntitiesInput = entities;
        return lastDisplayResultSetsOutput = resultSets;
    }

    private void applyDisplayResultSets(DisplayResultSet[] displayResultSets) {
        for (int i = 0, n = displayResultSets.length; i < n; i++)
            filterDisplays.get(i).setDisplayResultSet(displayResultSets[i]);
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
        Property<DisplayResultSet> displayResultSetProperty;
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

        Property<DisplayResultSet> getDisplayResultSetProperty() {
            return displayResultSetProperty;
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

        void setDisplayResultSetProperty(Property<DisplayResultSet> displayResultSetProperty) {
            this.displayResultSetProperty = displayResultSetProperty;
        }

        void setDisplayResultSet(DisplayResultSet rs) {
            displayResultSetProperty.setValue(rs);
            if (selectFirstRowOnFirstDisplay && rs.getRowCount() > 0) {
                selectFirstRowOnFirstDisplay = false;
                displaySelectionProperty.setValue(DisplaySelection.createSingleRowSelection(0));
            }
        }

        void resetDisplayResultSet(boolean empty) {
            if (empty)
                setEmptyDisplayResultSet();
            else
                displayResultSetProperty.setValue(entitiesListToDisplayResultSet(getCurrentEntityList()));
        }

        void setEmptyDisplayResultSet() {
            displayResultSetProperty.setValue(emptyDisplayResultSet());
        }

        DisplayResultSet entitiesListToDisplayResultSet(List<E> entities) {
            collectColumnsPersistentTerms();
            return EntityListToDisplayResultSetGenerator.createDisplayResultSet(entities, expressionColumns, i18n);
        }

        DisplayResultSet emptyDisplayResultSet() {
            return entitiesListToDisplayResultSet(emptyFutureList());
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