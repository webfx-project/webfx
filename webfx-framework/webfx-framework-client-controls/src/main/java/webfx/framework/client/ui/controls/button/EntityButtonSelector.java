package webfx.framework.client.ui.controls.button;

import javafx.application.Platform;
import javafx.beans.property.ObjectProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Pos;
import javafx.scene.Cursor;
import javafx.scene.Node;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import webfx.framework.shared.expression.Expression;
import webfx.framework.shared.expression.terms.ExpressionArray;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.client.ui.filter.ExpressionColumn;
import webfx.framework.client.ui.filter.ReactiveExpressionFilter;
import webfx.framework.client.ui.filter.StringFilter;
import webfx.framework.client.ui.filter.StringFilterBuilder;
import webfx.fxkit.extra.cell.renderer.ValueRenderer;
import webfx.fxkit.extra.cell.renderer.ValueRendererFactory;
import webfx.fxkit.extra.cell.renderer.ValueRenderingContext;
import webfx.fxkit.extra.controls.displaydata.datagrid.DataGrid;
import webfx.fxkit.extra.controls.displaydata.datagrid.SkinnedDataGrid;
import webfx.fxkit.extra.displaydata.DisplayResult;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.Strings;
import webfx.platform.shared.util.collection.Collections;
import webfx.platform.shared.util.function.Callable;

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
    private String searchCondition;

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
            DomainModel domainModel = dataSourceModel.getDomainModel();
            Object domainClassId = stringFilter.getDomainClassId();
            DomainClass entityClass = domainModel.getClass(domainClassId);
            if (stringFilter.getColumns() != null) {
                ExpressionColumn[] expressionColumns = ExpressionColumn.fromJsonArrayOrExpressionsDefinition(stringFilter.getColumns(), domainModel, domainClassId);
                renderingExpression = new ExpressionArray<>(Arrays.map(expressionColumns, expressionColumn -> expressionColumn.parseExpressionDefinitionIfNecessary(domainModel, domainClassId).getDisplayExpression(), Expression[]::new));
            } else
                renderingExpression = entityClass.getForeignFields();
            if (renderingExpression == null && stringFilter.getFields() != null)
                renderingExpression = entityClass.parseExpression(stringFilter.getFields());
            searchCondition = entityClass.getSearchCondition();
        }
        entityRenderer = renderingExpression == null ? null : ValueRendererFactory.getDefault().createValueRenderer(renderingExpression.getType());
        forceDialogRebuiltOnNextShow();
    }

    public void setSearchCondition(String searchCondition) {
        this.searchCondition = searchCondition;
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
            if (isSearchEnabled())
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

    @Override
    public boolean isSearchEnabled() {
        return super.isSearchEnabled() && searchCondition != null;
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

    private ReactiveExpressionFilter<E> getEntityDialogFilter() {
        if (entityDialogFilter == null)
            getOrCreateDialogContent();
        return entityDialogFilter;
    }

    @Override
    protected void setUpDialog(boolean show) {
        super.setUpDialog(show);
        getEntityDialogFilter().setActive(true);
    }

    @Override
    protected void startLoading() {
        if (!getEntityDialogFilter().isStarted())
            entityDialogFilter.start();
    }

    @Override
    protected void onDialogOk() {
        setSelectedItem(getEntityDialogFilter().getSelectedEntity());
        super.onDialogOk();
    }

    @Override
    protected void closeDialog() {
        getEntityDialogFilter().setActive(false);
        super.closeDialog();
    }
}
