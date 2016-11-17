package naga.framework.ui.filter;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.commons.type.PrimType;
import naga.commons.util.async.Handler;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Converter;
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
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.framework.ui.i18n.I18n;
import naga.framework.ui.mapping.EntityListToDisplayResultSetGenerator;
import naga.framework.ui.rx.RxFuture;
import naga.framework.ui.rx.RxScheduler;
import naga.framework.ui.rx.RxUi;
import naga.platform.json.spi.JsonArray;
import naga.platform.json.spi.JsonObject;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.spi.Platform;
import naga.toolkit.display.DisplayColumnBuilder;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.toolkit.spi.Toolkit;
import rx.Observable;

import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author Bruno Salmon
 */
public class ReactiveExpressionFilter {

    private final List<Observable<StringFilter>> stringFilterObservables = new ArrayList<>();
    private DataSourceModel dataSourceModel;
    private I18n i18n;
    private EntityStore store;
    private Object listId = "default";
    private boolean autoRefresh = false;
    private boolean startsWithEmptyResult = true;
    private Object domainClassId;
    private StringFilter baseFilter;
    private Handler<EntityList> entitiesHandler;
    private FilterDisplay filterDisplay;
    private List<FilterDisplay> filterDisplays = new ArrayList<>();
    private ReferenceResolver rootAliasReferenceResolver;
    private final Property<Boolean> activeProperty = new SimpleObjectProperty<>(true);

    public ReactiveExpressionFilter() {
    }

    public ReactiveExpressionFilter(Object jsonOrClass) {
        combine(new StringFilterBuilder(jsonOrClass));
    }

    public ReactiveExpressionFilter setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
        dataSourceModel.getDomainModel(); // Ensuring the data model is loaded with formats registered before expression columns are set
        return this;
    }

    public ReactiveExpressionFilter setI18n(I18n i18n) {
        this.i18n = i18n;
        return this;
    }

    public ReactiveExpressionFilter setStore(EntityStore store) {
        this.store = store;
        return this;
    }

    public ReactiveExpressionFilter setListId(Object listId) {
        this.listId = listId;
        return this;
    }

    public ReactiveExpressionFilter setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
        return this;
    }

    public ReactiveExpressionFilter setStartsWithEmptyResult(boolean startsWithEmptyResult) {
        this.startsWithEmptyResult = startsWithEmptyResult;
        return this;
    }

    public ReactiveExpressionFilter setEntitiesHandler(Handler<EntityList> entitiesHandler) {
        this.entitiesHandler = entitiesHandler;
        return this;
    }

    public Property<Boolean> activePropertyProperty() {
        return activeProperty;
    }

    public boolean isActive() {
        return activeProperty.getValue();
    }

    public ReactiveExpressionFilter combine(String json) {
        return combine(new StringFilter(json));
    }

    public ReactiveExpressionFilter combine(JsonObject json) {
        return combine(new StringFilter(json));
    }

    public ReactiveExpressionFilter combine(StringFilterBuilder stringFilterBuilder) {
        return combine(stringFilterBuilder.build());
    }

    public ReactiveExpressionFilter combine(StringFilter stringFilter) {
        if (domainClassId == null) {
            domainClassId = stringFilter.getDomainClassId();
            baseFilter = stringFilter;
        }
        return combine(Observable.just(stringFilter));
    }

    public ReactiveExpressionFilter combine(Observable<StringFilter> stringFilterObservable) {
        stringFilterObservables.add(stringFilterObservable);
        return this;
    }

    public <T> ReactiveExpressionFilter combine(Property<T> property, Converter<T, String> toJsonFilterConverter) {
        return combine(RxUi.observe(property)
                .map(t -> {
                    String json = toJsonFilterConverter.convert(t);
                    return json == null ? null : new StringFilter(json);
                }));
    }

    public ReactiveExpressionFilter combine(Property<Boolean> ifProperty, StringFilterBuilder stringFilterBuilder) {
        return combine(ifProperty, stringFilterBuilder.build());
    }

    public ReactiveExpressionFilter combine(Property<Boolean> ifProperty, String json) {
        return combine(ifProperty, new StringFilter(json));
    }

    public ReactiveExpressionFilter combine(Property<Boolean> ifProperty, StringFilter stringFilter) {
        return combine(RxUi.observeIf(Observable.just(stringFilter), ifProperty));
    }

    public ReactiveExpressionFilter nextDisplay() {
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
        if (filterDisplay == null)
            filterDisplays.add(filterDisplay = new FilterDisplay());
        return filterDisplay;
    }

    public ReactiveExpressionFilter setDisplaySelectionProperty(Property<DisplaySelection> displaySelectionProperty) {
        getFilterDisplay().setDisplaySelectionProperty(displaySelectionProperty);
        return this;
    }

    public ReactiveExpressionFilter setSelectedEntityHandler(Handler<Entity> entityHandler) {
        getFilterDisplay().setSelectedEntityHandler(entityHandler);
        return this;
    }

    public ReactiveExpressionFilter setSelectedEntityHandler(Property<DisplaySelection> displaySelectionProperty, Handler<Entity> entityHandler) {
        getFilterDisplay().setSelectedEntityHandler(displaySelectionProperty, entityHandler);
        return this;
    }

    public ReactiveExpressionFilter selectFirstRowOnFirstDisplay() {
        getFilterDisplay().selectFirstRowOnFirstDisplay();
        return this;
    }

    public ReactiveExpressionFilter selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty, Property onEachChangeProperty) {
        getFilterDisplay().selectFirstRowOnFirstDisplay(displaySelectionProperty, onEachChangeProperty);
        return this;
    }

    public ReactiveExpressionFilter selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty) {
        getFilterDisplay().selectFirstRowOnFirstDisplay(displaySelectionProperty);
        return this;
    }

    public ReactiveExpressionFilter setExpressionColumns(String jsonArrayDisplayColumns) {
        getFilterDisplay().setExpressionColumns(jsonArrayDisplayColumns);
        return this;
    }

    public ReactiveExpressionFilter setExpressionColumns(JsonArray array) {
        getFilterDisplay().setExpressionColumns(array);
        return this;
    }

    public ReactiveExpressionFilter setExpressionColumns(ExpressionColumn... expressionColumns) {
        getFilterDisplay().setExpressionColumns(expressionColumns);
        return this;
    }

    public ReactiveExpressionFilter applyDomainModelRowStyle() {
        getFilterDisplay().applyDomainModelRowStyle();
        return this;
    }

    public ReactiveExpressionFilter displayResultSetInto(Property<DisplayResultSet> displayResultSetProperty) {
        getFilterDisplay().setDisplayResultSetProperty(displayResultSetProperty);
        return this;
    }


    public Property<DisplaySelection> getDisplaySelectionProperty() {
        return getDisplaySelectionProperty(0);
    }

    public Property<DisplaySelection> getDisplaySelectionProperty(int displayIndex) {
        return filterDisplays.get(displayIndex).getDisplaySelectionProperty();
    }

    public Entity getSelectedEntity() {
        return getSelectedEntity(0);
    }

    public Entity getSelectedEntity(int displayIndex) {
        return filterDisplays.get(displayIndex).getSelectedEntity();
    }

    public Entity getSelectedEntity(DisplaySelection selection) {
        return getSelectedEntity(0, selection);
    }

    public Entity getSelectedEntity(int displayIndex, DisplaySelection selection) {
        return filterDisplays.get(displayIndex).getSelectedEntity(selection);
    }

    public EntityList getCurrentEntityList() {
        return getCurrentEntityList(0);
    }

    public EntityList getCurrentEntityList(int displayIndex) {
        return filterDisplays.get(displayIndex).getCurrentEntityList();
    }

    public ReactiveExpressionFilter start() {
        // If not set, we create a new store
        if (store == null)
            store = EntityStore.create(dataSourceModel);
        combine(activeProperty, "{}");
        // if autoRefresh is set, we combine the filter with a 5s tic tac property
        if (autoRefresh) {
            Property<Boolean> ticTacProperty = new SimpleObjectProperty<>(true);
            Platform.schedulePeriodic(5000, () -> {if (isActive()) ticTacProperty.setValue(!ticTacProperty.getValue());});
            combine(ticTacProperty, "{}");
        }
        // The following call is to set stringFilterObservableLastIndex on the latest filterDisplay
        goToNextFilterDisplayIfDisplayResultSetPropertyIsSet();
        // Initializing the display with empty results (no rows but columns) so the component (probably a table) display the columns before calling the server
        if (startsWithEmptyResult)
            resetAllDisplayResultSets(true);
        // Also adding a listener reacting to a language change by updating the columns translations immediately (without making a new server request)
        if (i18n != null)
            i18n.dictionaryProperty().addListener((observable, oldValue, newValue) -> resetAllDisplayResultSets(false));
        AtomicInteger querySequence = new AtomicInteger(); // Used for skipping possible too old query results
        Observable<StringFilter> resultingStringFilterObservable = Observable
                .combineLatest(stringFilterObservables, this::mergeStringFilters)
                .filter(stringFilter -> isActive());
        if (!autoRefresh)
            resultingStringFilterObservable = resultingStringFilterObservable.distinctUntilChanged();
        Observable<EntityList> entitiesObservable = resultingStringFilterObservable.switchMap(stringFilter -> {
            // Shortcut: when the string filter is "false", we return an empty entity list immediately (no server call)
            if ("false".equals(stringFilter.getWhere()))
                return Observable.just(EntityList.create(listId, store));
            // Otherwise we compile the final string filter into sql
            SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect(stringFilter.toStringSelect());
            // We increment and capture the sequence to check if the request is still the latest one when receiving the result
            int sequence = querySequence.incrementAndGet();
            // Then we ask the query service to execute the sql query
            return RxFuture.from(Platform.getQueryService().executeQuery(new QueryArgument(sqlCompiled.getSql(), dataSourceModel.getId())))
                    // Aborting the process (returning null) if the sequence differs (meaning a new request has been sent)
                    // Otherwise transforming the QueryResultSet into an EntityList
                    .map(queryResultSet -> (sequence != querySequence.get()) ? null : queryResultSetToEntities(queryResultSet, sqlCompiled));
        });
        if (!filterDisplays.isEmpty() && filterDisplays.get(0).displayResultSetProperty != null)
            entitiesObservable
                // Finally transforming the EntityList into a DisplayResultSet
                .map(this::entitiesToDisplayResultSets)
                .observeOn(RxScheduler.UI_SCHEDULER)
                .subscribe(this::applyDisplayResultSets);
        else if (entitiesHandler != null)
            entitiesObservable.subscribe(entitiesHandler::handle);
        return this;
    }

    private StringFilter mergeStringFilters(Object... args) {
        Iterator<FilterDisplay> it = filterDisplays.iterator();
        filterDisplay = Collections.next(it);
        StringFilterBuilder mergeBuilder = new StringFilterBuilder(domainClassId);
        for (int i = 0; i < args.length; i++) {
            mergeBuilder.merge((StringFilter) args[i]);
            if (filterDisplay != null && filterDisplay.stringFilterObservableLastIndex == i) {
                if (mergeBuilder.getColumns() != null) {
                    filterDisplay.setExpressionColumns(mergeBuilder.getColumns());
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
        Toolkit.get().scheduler().runInUiThread(() -> {
            for (FilterDisplay filterDisplay : filterDisplays)
                filterDisplay.resetDisplayResultSet(empty);
        });
    }

    private EntityList queryResultSetToEntities(QueryResultSet rs, SqlCompiled sqlCompiled) {
        return QueryResultSetToEntityListGenerator.createEntityList(rs, sqlCompiled.getQueryMapping(), store, listId);
    }

    private DisplayResultSet[] entitiesToDisplayResultSets(EntityList entities) {
        if (entitiesHandler != null)
            entitiesHandler.handle(entities);
        int n = filterDisplays.size();
        DisplayResultSet[] resultSets = new DisplayResultSet[n];
        for (int i = 0; i < n; i++)
            resultSets[i] = filterDisplays.get(i).entitiesListToDisplayResultSet(entities);
        return resultSets;
    }

    private void applyDisplayResultSets(DisplayResultSet[] displayResultSets) {
        for (int i = 0, n = displayResultSets.length; i < n; i++)
            filterDisplays.get(i).setDisplayResultSet(displayResultSets[i]);
    }

    private ReferenceResolver getRootAliasReferenceResolver() {
        if (rootAliasReferenceResolver == null) {
            DomainModel domainModel = dataSourceModel.getDomainModel();
            // Before parsing, we prepare a ReferenceResolver to resolve possible references to root aliases
            Map<String, Alias> rootAliases = new HashMap<>();
            rootAliasReferenceResolver = rootAliases::get;
            if (baseFilter != null) { // Root aliases are stored in the baseFilter
                // The first possible root alias is the base filter alias. Ex: Event e => the alias "e" then acts in a
                // similar way as "this" in java because it refers to the current Event row in the select, so some
                // expressions such as sub queries may refer to it (ex: select count(1) from Booking where event=e)
                String alias = baseFilter.getAlias();
                if (alias != null) // when defined, we add an Alias expression that can be returned when resolving this alias
                    rootAliases.put(alias, new Alias(alias, domainModel.getClass(domainClassId)));
                // Other possible root aliases can be As expressions defined in the base filter fields, such as sub queries
                // If fields contains for example (select ...) as xxx -> then xxx can be referenced in expression columns
                String fields = baseFilter.getFields();
                if (fields != null && fields.contains(" as ")) { // quick skipping if fields doesn't contains " as "
                    try {
                        ThreadLocalReferenceResolver.pushReferenceResolver(rootAliasReferenceResolver);
                        // Now that the ReferenceResolver is ready, we can parse the expression columns
                        for (Expression field : domainModel.parseExpressionArray(fields, domainClassId).getExpressions()) {
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

        Property<DisplaySelection> getDisplaySelectionProperty() {
            return displaySelectionProperty;
        }

        void setDisplaySelectionProperty(Property<DisplaySelection> displaySelectionProperty) {
            this.displaySelectionProperty = displaySelectionProperty;
        }

        Property<DisplayResultSet> getDisplayResultSetProperty() {
            return displayResultSetProperty;
        }

        void setSelectedEntityHandler(Handler<Entity> entityHandler) {
            displaySelectionProperty.addListener((observable, oldValue, newValue) -> entityHandler.handle(getSelectedEntity()));
        }

        void setSelectedEntityHandler(Property<DisplaySelection> displaySelectionProperty, Handler<Entity> entityHandler) {
            setDisplaySelectionProperty(displaySelectionProperty);
            setSelectedEntityHandler(entityHandler);
        }

        void selectFirstRowOnFirstDisplay() {
            selectFirstRowOnFirstDisplay = true;
        }

        void selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty, Property onEachChangeProperty) {
            // Each time the property change, we clear the selection and reset the selectFirstRowOnFirstDisplay to true to arm the mechanism again
            onEachChangeProperty.addListener(observable -> {
                displaySelectionProperty.setValue(null);
                selectFirstRowOnFirstDisplay();
            });
            selectFirstRowOnFirstDisplay(displaySelectionProperty);
        }

        void selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty) {
            setDisplaySelectionProperty(displaySelectionProperty);
            selectFirstRowOnFirstDisplay();
        }

        Entity getSelectedEntity() {
            return getSelectedEntity(displaySelectionProperty.getValue());
        }

        Entity getSelectedEntity(DisplaySelection selection) {
            Entity selectedEntity = null;
            int selectedRow = selection == null ? -1 : selection.getSelectedRow();
            if (selectedRow >= 0)
                selectedEntity = getCurrentEntityList().get(selectedRow);
            return selectedEntity;
        }

        EntityList getCurrentEntityList() {
            return store.getEntityList(listId);
        }

        void setExpressionColumns(String jsonArrayDisplayColumns) {
            setExpressionColumns(ExpressionColumn.fromJsonArray(jsonArrayDisplayColumns));
        }

        void setExpressionColumns(JsonArray array) {
            setExpressionColumns(ExpressionColumn.fromJsonArray(array));
        }

        void setExpressionColumns(ExpressionColumn... expressionColumns) {
            this.expressionColumns = expressionColumns;
            columnsPersistentTerms = null; // forcing re-computation on next collectColumnsPersistentTerms() call since the columns have changed
        }

        void applyDomainModelRowStyle() {
            DomainClass domainClass = dataSourceModel.getDomainModel().getClass(domainClassId);
            ExpressionArray rowStylesExpressionArray = domainClass.getStyleClassesExpressionArray();
            if (rowStylesExpressionArray != null) {
                ExpressionColumn[] includingRowStyleColumns = new ExpressionColumn[expressionColumns.length + 1];
                includingRowStyleColumns[0] = ExpressionColumn.create(rowStylesExpressionArray, DisplayColumnBuilder.create("style", PrimType.STRING).setRole("style").build());
                System.arraycopy(expressionColumns, 0, includingRowStyleColumns, 1, expressionColumns.length);
                setExpressionColumns(includingRowStyleColumns);
            }
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
                displayResultSetProperty.setValue(entitiesListToDisplayResultSet(store.getEntityList(listId)));
        }

        void setEmptyDisplayResultSet() {
            displayResultSetProperty.setValue(emptyDisplayResultSet());
        }

        DisplayResultSet entitiesListToDisplayResultSet(EntityList entities) {
            collectColumnsPersistentTerms();
            return EntityListToDisplayResultSetGenerator.createDisplayResultSet(entities, expressionColumns, i18n);
        }

        DisplayResultSet emptyDisplayResultSet() {
            return entitiesListToDisplayResultSet(EntityList.create(listId, store));
        }

        @SuppressWarnings("unchecked")
        List<Expression> collectColumnsPersistentTerms() {
            if (columnsPersistentTerms == null) {
                columnsPersistentTerms = new ArrayList<>();
                DomainModel domainModel = dataSourceModel.getDomainModel();
                if (expressionColumns != null)
                    try {
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