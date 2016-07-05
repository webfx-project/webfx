package naga.core.ui.rx;

import javafx.beans.property.Property;
import naga.core.json.JsonArray;
import naga.core.json.JsonObject;
import naga.core.orm.domainmodel.DataSourceModel;
import naga.core.orm.domainmodel.DomainClass;
import naga.core.orm.entity.Entity;
import naga.core.orm.entity.EntityList;
import naga.core.orm.entity.EntityStore;
import naga.core.orm.expression.Expression;
import naga.core.orm.expression.term.ExpressionArray;
import naga.core.orm.expressionsqlcompiler.sql.SqlCompiled;
import naga.core.orm.mapping.QueryResultSetToEntityListGenerator;
import naga.core.orm.stringfilter.StringFilter;
import naga.core.orm.stringfilter.StringFilterBuilder;
import naga.core.services.query.QueryArgument;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.Toolkit;
import naga.core.type.PrimType;
import naga.core.ui.displayresultset.DisplayColumn;
import naga.core.ui.displayresultset.DisplayResultSet;
import naga.core.ui.displayresultset.EntityListToDisplayResultSetGenerator;
import naga.core.ui.displayresultset.ExpressionColumn;
import naga.core.ui.displayselection.DisplaySelection;
import naga.core.util.async.Handler;
import naga.core.util.function.Converter;
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
            expressionColumns2[0] = ExpressionColumn.create(rowStylesExpressionArray, new DisplayColumn(null, "style", PrimType.STRING, "style"));
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

    private void checkFields() {
        if (store == null)
            store = new EntityStore();
        if (listId == null)
            listId = "default";
        List<Expression> displayPersistentTerms = new ArrayList<>();
        for (ExpressionColumn expressionColumn : expressionColumns) {
            expressionColumn.parseIfNecessary(dataSourceModel.getDomainModel(), domainClassId);
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
        Observable<DisplayResultSet> displayResultObservable = Observable
                .combineLatest(stringFilterObservables, StringFilterBuilder::mergeStringFilters)
                .distinctUntilChanged()
                .switchMap(stringFilter -> {
                    if ("false".equals(stringFilter.getWhere()))
                        return Observable.just(emptyDisplayResultSet());
                    SqlCompiled sqlCompiled = dataSourceModel.getDomainModel().compileSelect(stringFilter.toStringSelect());
                    Platform.log(sqlCompiled.getSql());
                    return RxFuture.from(Platform.query().executeQuery(new QueryArgument(sqlCompiled.getSql(), dataSourceModel.getId())))
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
        return EntityListToDisplayResultSetGenerator.createDisplayResultSet(new EntityList(listId, store), expressionColumns);
    }

}

