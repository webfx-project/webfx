package naga.framework.ui.controls;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import naga.framework.expression.Expression;
import naga.framework.expression.terms.ExpressionArray;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityStore;
import naga.framework.ui.action.ButtonFactoryMixin;
import naga.framework.ui.filter.ExpressionColumn;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.filter.StringFilter;
import naga.framework.ui.filter.StringFilterBuilder;
import naga.fxdata.cell.renderer.ValueRenderer;
import naga.fxdata.cell.renderer.ValueRendererFactory;
import naga.fxdata.control.DataGrid;
import naga.fxdata.displaydata.DisplayResultSet;
import naga.util.Arrays;
import naga.util.Strings;
import naga.util.collection.Collections;

/**
 * @author Bruno Salmon
 */
public class EntityButtonSelector<E extends Entity> extends ButtonSelector<E> {

    private final ObjectProperty<DisplayResultSet> deferredDisplayResultSet = new SimpleObjectProperty<>();
    private Object jsonOrClass;
    private final DataSourceModel dataSourceModel;
    private Expression renderingExpression;
    private ValueRenderer entityRenderer;

    private EntityStore loadingStore;
    private ReactiveExpressionFilter<E> entityDialogFilter;
    private DataGrid dataGrid;

    // Good to put a limit especially for low-end mobiles
    private int adaptiveLimit = 6; // starting with 6 entries (fit with drop down/up) but can be increased in modal in dependence of the available height

    public EntityButtonSelector(Object jsonOrClass, ButtonFactoryMixin buttonFactory, Pane parent, DataSourceModel dataSourceModel) {
        super(parent, buttonFactory);
        this.dataSourceModel = dataSourceModel;
        setJsonOrClass(jsonOrClass);
        setResizeProperty(deferredDisplayResultSet);
    }

    public void setJsonOrClass(Object jsonOrClass) {
        this.jsonOrClass = jsonOrClass;
        renderingExpression = null;
        if (jsonOrClass != null) {
            StringFilter stringFilter = new StringFilterBuilder(jsonOrClass).build();
            DomainClass entityClass = dataSourceModel.getDomainModel().getClass(stringFilter.getDomainClassId());
            if (stringFilter.getColumns() != null) {
                ExpressionColumn[] expressionColumns = ExpressionColumn.fromJsonArray(stringFilter.getColumns());
                renderingExpression = new ExpressionArray<>(Arrays.map(expressionColumns, expressionColumn -> expressionColumn.parseExpressionDefinitionIfNecessary(dataSourceModel.getDomainModel(), stringFilter.getDomainClassId()).getExpression(), Expression[]::new));
            } else if (stringFilter.getFields() != null)
                renderingExpression = entityClass.parseExpression(stringFilter.getFields());
            else
                renderingExpression = entityClass.getForeignFields();
        }
        entityRenderer = renderingExpression == null ? null : ValueRendererFactory.getDefault().createCellRenderer(renderingExpression.getType());
        forceDialogRebuiltOnNextShow();
    }

    public void setLoadingStore(EntityStore loadingStore) {
        this.loadingStore = loadingStore;
    }

    @Override
    protected Node getOrCreateButtonContentFromSelectedItem() {
        E entity = getSelectedItem();
        Object renderedValue = entity == null ? null : entity.evaluate(renderingExpression);
        return entityRenderer.renderCellValue(renderedValue);
    }

    @Override
    protected Region getOrCreateDialogContent() {
        if (dataGrid == null && entityRenderer != null) {
            dataGrid = new DataGrid();
            dataGrid.setHeaderVisible(false);
            dataGrid.setFullHeight(true);
            BorderPane.setAlignment(dataGrid, Pos.TOP_LEFT);
            EntityStore filterStore = loadingStore != null ? loadingStore : getSelectedItem() != null ? getSelectedItem().getStore() : null;
            entityDialogFilter = new ReactiveExpressionFilter<E>(jsonOrClass).setDataSourceModel(dataSourceModel).setI18n(getButtonFactory()).setStore(filterStore);
            String searchCondition = entityDialogFilter.getDomainClass().getSearchCondition();
            if (searchCondition != null) {
                entityDialogFilter.combine(searchTextProperty(), s -> {
                    if (Strings.isEmpty(s))
                        return null;
                    setSearchParameters(s, entityDialogFilter.getStore());
                    return "{where: `" + searchCondition + "`}";
                });
            }
            entityDialogFilter
                    .combine(dialogHeightProperty(), height -> "{limit: " + updateAdaptiveLimit(height) + "}")
                    .setExpressionColumns(ExpressionColumn.create(renderingExpression))
                    .displayResultSetInto(dataGrid.displayResultSetProperty())
                    .setDisplaySelectionProperty(dataGrid.displaySelectionProperty())
                    //.setSelectedEntityHandler(dataGrid.displaySelectionProperty(), o -> onOkEntityDialog())
                    .start();
            dataGrid.setOnMouseClicked(e -> {if (e.getClickCount() == 1) onDialogOk(); });
            dataGrid.displayResultSetProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> deferredDisplayResultSet.setValue(newValue)));
        }
        return dataGrid;
    }

    private int updateAdaptiveLimit(Number height) {
        int maxNumberOfVisibleEntries = height.intValue() / 36;
        if (maxNumberOfVisibleEntries > adaptiveLimit)
            adaptiveLimit = maxNumberOfVisibleEntries + 6; // extra 6 to avoid repetitive requests when resizing window
        return adaptiveLimit;
    }

    protected void setSearchParameters(String search, EntityStore store) {
        store.setParameterValue("search", search);
        store.setParameterValue("lowerSearch", search.toLowerCase());
        store.setParameterValue("searchLike", "%" + search + "%");
        store.setParameterValue("lowerSearchLike", "%" + search.toLowerCase() + "%");
    }

    public void autoSelectFirstEntity() {
        setUpDialog(false);
        if (entityDialogFilter != null)
            entityDialogFilter.setEntitiesHandler(entityList -> setSelectedItem(Collections.first(entityList)));
    }

    @Override
    protected void setUpDialog(boolean show) {
        super.setUpDialog(show);
        entityDialogFilter.setActive(true);
    }

    @Override
    protected void onDialogOk() {
        super.onDialogOk();
        setSelectedItem(entityDialogFilter.getSelectedEntity());
    }

    @Override
    protected void closeDialog() {
        super.closeDialog();
        entityDialogFilter.setActive(false);
    }
}
