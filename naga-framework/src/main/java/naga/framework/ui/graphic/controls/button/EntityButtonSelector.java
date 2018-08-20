package naga.framework.ui.graphic.controls.button;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
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
import naga.framework.ui.filter.ExpressionColumn;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.filter.StringFilter;
import naga.framework.ui.filter.StringFilterBuilder;
import naga.fxdata.cell.renderer.ValueRenderer;
import naga.fxdata.cell.renderer.ValueRendererFactory;
import naga.fxdata.cell.renderer.ValueRenderingContext;
import naga.fxdata.control.DataGrid;
import naga.fxdata.control.SkinnedDataGrid;
import naga.fxdata.displaydata.DisplayResult;
import naga.util.Arrays;
import naga.util.Strings;
import naga.util.collection.Collections;
import naga.util.function.Callable;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class EntityButtonSelector<E extends Entity> extends ButtonSelector<E> {

    private final ObjectProperty<DisplayResult> deferredDisplayResult = new SimpleObjectProperty<>();
    private Object jsonOrClass;
    private final DataSourceModel dataSourceModel;
    private Expression<E> renderingExpression;
    private ValueRenderer entityRenderer;

    private EntityStore loadingStore;
    private ReactiveExpressionFilter<E> entityDialogFilter;
    private List<E> restrictedFilterList;
    private DataGrid dialogDataGrid;

    // Good to put a limit especially for low-end mobiles
    private int adaptiveLimit = 6; // starting with 6 entries (fit with drop down/up) but can be increased in modal in dependence of the available height

    public EntityButtonSelector(Object jsonOrClass, ButtonFactoryMixin buttonFactory, Callable<Pane> parentGetter, DataSourceModel dataSourceModel) {
        this(jsonOrClass, buttonFactory, parentGetter, null, dataSourceModel);
    }

    public EntityButtonSelector(Object jsonOrClass, ButtonFactoryMixin buttonFactory, Pane parent, DataSourceModel dataSourceModel) {
        this(jsonOrClass, buttonFactory, null, parent, dataSourceModel);
    }

    protected EntityButtonSelector(Object jsonOrClass, ButtonFactoryMixin buttonFactory, Callable<Pane> parentGetter, Pane parent, DataSourceModel dataSourceModel) {
        super(buttonFactory, parentGetter, parent);
        this.dataSourceModel = dataSourceModel;
        setJsonOrClass(jsonOrClass);
        setLoadedContentProperty(deferredDisplayResult);
    }

    public List<E> getRestrictedFilterList() {
        return restrictedFilterList;
    }

    public void setRestrictedFilterList(List<E> restrictedFilterList) {
        this.restrictedFilterList = restrictedFilterList;
    }

    public void setJsonOrClass(Object jsonOrClass) {
        this.jsonOrClass = jsonOrClass;
        renderingExpression = null;
        if (jsonOrClass != null) {
            StringFilter stringFilter = new StringFilterBuilder(jsonOrClass).build();
            DomainClass entityClass = dataSourceModel.getDomainModel().getClass(stringFilter.getDomainClassId());
            if (stringFilter.getColumns() != null) {
                ExpressionColumn[] expressionColumns = ExpressionColumn.fromJsonArray(stringFilter.getColumns());
                renderingExpression = new ExpressionArray<>(Arrays.map(expressionColumns, expressionColumn -> expressionColumn.parseExpressionDefinitionIfNecessary(dataSourceModel.getDomainModel(), stringFilter.getDomainClassId()).getDisplayExpression(), Expression[]::new));
            } else if (stringFilter.getFields() != null)
                renderingExpression = entityClass.parseExpression(stringFilter.getFields());
            else
                renderingExpression = entityClass.getForeignFields();
        }
        entityRenderer = renderingExpression == null ? null : ValueRendererFactory.getDefault().createValueRenderer(renderingExpression.getType());
        forceDialogRebuiltOnNextShow();
    }

    public void setLoadingStore(EntityStore loadingStore) {
        this.loadingStore = loadingStore;
    }

    @Override
    protected Node getOrCreateButtonContentFromSelectedItem() {
        E entity = getSelectedItem();
        Object renderedValue = entity == null ? null : entity.evaluate(renderingExpression);
        return entityRenderer.renderValue(renderedValue, ValueRenderingContext.DEFAULT_READONLY_CONTEXT);
    }

    @Override
    protected Region getOrCreateDialogContent() {
        if (dialogDataGrid == null && entityRenderer != null) {
            dialogDataGrid = new SkinnedDataGrid(); // Better rendering in desktop JavaFx (but might be slower in web version)
            dialogDataGrid.setHeaderVisible(false);
            dialogDataGrid.setCursor(Cursor.HAND);
            BorderPane.setAlignment(dialogDataGrid, Pos.TOP_LEFT);
            dialogDataGrid.displayResultProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> deferredDisplayResult.setValue(newValue)));
            EntityStore filterStore = loadingStore != null ? loadingStore : getSelectedItem() != null ? getSelectedItem().getStore() : null;
            entityDialogFilter = new ReactiveExpressionFilter<E>(jsonOrClass)
                    .setDataSourceModel(dataSourceModel)
                    .setStore(filterStore)
                    .setRestrictedFilterList(restrictedFilterList)
                    .setExpressionColumns(ExpressionColumn.create(renderingExpression))
                    .displayResultInto(dialogDataGrid.displayResultProperty())
                    .setDisplaySelectionProperty(dialogDataGrid.displaySelectionProperty())
                    .setSelectedEntityHandler(dialogDataGrid.displaySelectionProperty(), e -> {if (e != null) onDialogOk();})
            ;
            String searchCondition = entityDialogFilter.getDomainClass().getSearchCondition();
            if (searchCondition != null && isSearchEnabled())
                entityDialogFilter
                    .combine(searchTextProperty(), s -> {
                        if (Strings.isEmpty(s))
                            return null;
                        setSearchParameters(s, entityDialogFilter.getStore());
                        return "{where: `" + searchCondition + "`}";
                    })
                    .combine(dialogHeightProperty(), height -> "{limit: " + updateAdaptiveLimit(height) + "}");
            //dialogDataGrid.setOnMouseClicked(e -> {if (e.isPrimaryButtonDown() && e.getClickCount() == 1) onDialogOk(); });
        }
        return dialogDataGrid;
    }

    private int updateAdaptiveLimit(Number height) {
        int maxNumberOfVisibleEntries = height.intValue() / 36;
        if (maxNumberOfVisibleEntries > adaptiveLimit)
            adaptiveLimit = maxNumberOfVisibleEntries + (getDecidedShowMode() == ShowMode.MODAL_DIALOG ? 6 : 0); // extra 6 to avoid repetitive requests when resizing window
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
    protected void startLoading() {
        if (!entityDialogFilter.isStarted())
            entityDialogFilter.start();
    }

    @Override
    protected void onDialogOk() {
        setSelectedItem(entityDialogFilter.getSelectedEntity());
        super.onDialogOk();
    }

    @Override
    protected void closeDialog() {
        entityDialogFilter.setActive(false);
        super.closeDialog();
    }
}
