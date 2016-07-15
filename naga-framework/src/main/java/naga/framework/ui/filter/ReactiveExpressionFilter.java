package naga.framework.ui.filter;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import naga.platform.json.spi.JsonArray;
import naga.platform.json.spi.JsonObject;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityList;
import naga.framework.orm.entity.EntityStore;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.expression.sqlcompiler.sql.SqlCompiled;
import naga.framework.ui.mapping.EntityListToDisplayResultSetGenerator;
import naga.framework.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.platform.services.query.QueryArgument;
import naga.framework.ui.rx.RxFuture;
import naga.framework.ui.rx.RxUi;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;
import naga.commons.type.PrimType;
import naga.toolkit.display.DisplayColumn;
import naga.toolkit.display.DisplayResultSet;
import naga.toolkit.display.DisplaySelection;
import naga.commons.util.async.Handler;
import naga.commons.util.function.Converter;
import rx.Observable;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public class ReactiveExpressionFilter {

    private final List<Observable<StringFilter>> stringFilterObservables = new ArrayList<>();
    private Object domainClassId;
    private ExpressionColumn[] expressionColumns;
    private DataSourceModel dataSourceModel;
    private EntityStore store;
    private Object listId;
    private Property<DisplaySelection> displaySelectionProperty;
    private boolean selectFirstRowOnFirstDisplay;
    private boolean autoRefresh = false;

    public ReactiveExpressionFilter() {
    }

    public ReactiveExpressionFilter(Object jsonOrClass) {
        combine(new StringFilterBuilder(jsonOrClass));
    }

    public ReactiveExpressionFilter setDataSourceModel(DataSourceModel dataSourceModel) {
        this.dataSourceModel = dataSourceModel;
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

    public Property<DisplaySelection> getDisplaySelectionProperty() {
        return displaySelectionProperty;
    }

    public ReactiveExpressionFilter setDisplaySelectionProperty(Property<DisplaySelection> displaySelectionProperty) {
        this.displaySelectionProperty = displaySelectionProperty;
        return this;
    }

    public ReactiveExpressionFilter setSelectedEntityHandler(Handler<Entity> entityHandler) {
        this.displaySelectionProperty.addListener((observable, oldValue, newValue) -> entityHandler.handle(getSelectedEntity()));
        return this;
    }

    public ReactiveExpressionFilter setSelectedEntityHandler(Property<DisplaySelection> displaySelectionProperty, Handler<Entity> entityHandler) {
        return setDisplaySelectionProperty(displaySelectionProperty).setSelectedEntityHandler(entityHandler);
    }

    public ReactiveExpressionFilter selectFirstRowOnFirstDisplay() {
        this.selectFirstRowOnFirstDisplay = true;
        return this;
    }

    public ReactiveExpressionFilter selectFirstRowOnFirstDisplay(Property<DisplaySelection> displaySelectionProperty) {
        return setDisplaySelectionProperty(displaySelectionProperty).selectFirstRowOnFirstDisplay();
    }

    public Entity getSelectedEntity() {
        return getSelectedEntity(displaySelectionProperty.getValue());
    }

    public Entity getSelectedEntity(DisplaySelection selection) {
        Entity selectedEntity = null;
        int selectedRow = selection == null ? -1 : selection.getSelectedRow();
        if (selectedRow >= 0)
            selectedEntity = getCurrentEntityList().get(selectedRow);
        return selectedEntity;
    }

    public EntityList getCurrentEntityList() {
        return store.getEntityList(listId);
    }

    public ReactiveExpressionFilter setExpressionColumns(String jsonArrayDisplayColumns) {
        return setExpressionColumns(ExpressionColumn.fromJsonArray(jsonArrayDisplayColumns));
    }

    public ReactiveExpressionFilter setExpressionColumns(JsonArray array) {
        return setExpressionColumns(ExpressionColumn.fromJsonArray(array));
    }

    public ReactiveExpressionFilter setExpressionColumns(ExpressionColumn... expressionColumns) {
        this.expressionColumns = expressionColumns;
        return this;
    }

    public ReactiveExpressionFilter applyDomainModelRowStyle() {
        DomainClass domainClass = dataSourceModel.getDomainModel().getClass(domainClassId);
        ExpressionArray rowStylesExpressionArray = domainClass.getStyleClassesExpressionArray();
        if (rowStylesExpressionArray != null) {
            ExpressionColumn[] expressionColumns2 = new ExpressionColumn[expressionColumns.length + 1];
            expressionColumns2[0] = ExpressionColumn.create(rowStylesExpressionArray, DisplayColumn.create(null, "style", PrimType.STRING, "style"));
            System.arraycopy(expressionColumns, 0, expressionColumns2, 1, expressionColumns.length);
            expressionColumns = expressionColumns2;
        }
        return this;
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
        if (domainClassId == null)
            domainClassId = stringFilter.getDomainClassId();
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

    public ReactiveExpressionFilter setAutoRefresh(boolean autoRefresh) {
        this.autoRefresh = autoRefresh;
        return this;
    }

    private void checkFields() {
        if (store == null)
            store = EntityStore.create(dataSourceModel);
        if (listId == null)
            listId = "default";
        List<Expression> displayPersistentTerms = new ArrayList<>();
        for (ExpressionColumn expressionColumn : expressionColumns) {
            expressionColumn.parseExpressionDefinitionIfNecessary(dataSourceModel.getDomainModel(), domainClassId);
            expressionColumn.getExpression().collectPersistentTerms(displayPersistentTerms);
        }
        if (!displayPersistentTerms.isEmpty())
            combine(new StringFilterBuilder().setFields(new ExpressionArray<>(displayPersistentTerms).toString()));
    }

    public ReactiveExpressionFilter displayResultSetInto(Property<DisplayResultSet> displayResultSetProperty) {
        checkFields();
        // Emitting an initial empty display result (no rows but columns) to initialize the component (probably a table) with the columns before calling the server
        if (displayResultSetProperty.getValue() == null && expressionColumns != null)
            Toolkit.get().scheduler().runInUiThread(() -> displayResultSetProperty.setValue(emptyDisplayResultSet()));
        if (autoRefresh) {
            Property<Boolean> ticTacProperty = new SimpleObjectProperty<>(true);
            Platform.schedulePeriodic(5000, () -> ticTacProperty.setValue(!ticTacProperty.getValue()));
            combine(ticTacProperty, "{}");
        }
        Observable<DisplayResultSet> displayResultObservable = Observable
                .combineLatest(stringFilterObservables, StringFilterBuilder::mergeStringFilters)
                //.distinctUntilChanged() // commented to allow auto refresh
                .switchMap(stringFilter -> {
                    if ("false".equals(stringFilter.getWhere()))
                        return Observable.just(emptyDisplayResultSet());
                    SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect(stringFilter.toStringSelect());
                    Platform.log(sqlCompiled.getSql());
                    return RxFuture.from(Platform.getQueryService().executeQuery(new QueryArgument(sqlCompiled.getSql(), dataSourceModel.getId())))
                            .map(sqlReadResult -> QueryResultSetToEntityListGenerator.createEntityList(sqlReadResult, sqlCompiled.getQueryMapping(), store, listId))
                            .map(entities -> {
                                if (selectFirstRowOnFirstDisplay && entities.size() > 0) {
                                    selectFirstRowOnFirstDisplay = false;
                                    displaySelectionProperty.setValue(DisplaySelection.createSingleRowSelection(0)); // Temporary implementation
                                }
                                return EntityListToDisplayResultSetGenerator.createDisplayResultSet(entities, expressionColumns);
                            });
                });
        RxUi.displayObservable(displayResultObservable, displayResultSetProperty);
        return this;
    }

    private DisplayResultSet emptyDisplayResultSet() {
        return EntityListToDisplayResultSetGenerator.createDisplayResultSet(EntityList.create(listId, store), expressionColumns);
    }

}

