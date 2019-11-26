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
import webfx.extras.cell.renderer.ValueRenderer;
import webfx.extras.cell.renderer.ValueRendererFactory;
import webfx.extras.cell.renderer.ValueRenderingContext;
import webfx.extras.visual.VisualResult;
import webfx.extras.visual.controls.grid.SkinnedVisualGrid;
import webfx.extras.visual.controls.grid.VisualGrid;
import webfx.framework.client.orm.entity.filter.DqlStatement;
import webfx.framework.client.orm.entity.filter.DqlStatementBuilder;
import webfx.framework.client.orm.entity.filter.table.EntityColumn;
import webfx.framework.client.orm.entity.filter.visual.ReactiveVisualFilter;
import webfx.framework.client.orm.entity.filter.visual.VisualEntityColumnFactory;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.function.Callable;

import java.util.List;
import java.util.function.Predicate;

import static webfx.framework.client.orm.entity.filter.DqlStatement.limit;
import static webfx.framework.client.orm.entity.filter.DqlStatement.where;

/**
 * @author Bruno Salmon
 */
public class EntityButtonSelector<E extends Entity> extends ButtonSelector<E> {

    private final ObjectProperty<VisualResult> deferredVisualResult = new SimpleObjectProperty<>();
    private Object jsonOrClass;
    private final DataSourceModel dataSourceModel;
    private Expression<E> renderingExpression;
    private ValueRenderer entityRenderer;

    private EntityStore loadingStore;
    private ReactiveVisualFilter<E> entityDialogFilter;
    private List<E> restrictedFilterList;
    private VisualGrid dialogVisualGrid;
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
        setLoadedContentProperty(deferredVisualResult);
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
            DqlStatement dqlStatement = new DqlStatementBuilder(jsonOrClass).build();
            DomainModel domainModel = dataSourceModel.getDomainModel();
            Object domainClassId = dqlStatement.getDomainClassId();
            DomainClass entityClass = domainModel.getClass(domainClassId);
            if (dqlStatement.getColumns() != null) {
                EntityColumn<E>[] entityColumns = VisualEntityColumnFactory.get().fromJsonArrayOrExpressionsDefinition(dqlStatement.getColumns(), entityClass);
                renderingExpression = new ExpressionArray<>(Arrays.map(entityColumns, expressionColumn -> expressionColumn.parseExpressionDefinitionIfNecessary(entityClass).getDisplayExpression(), Expression[]::new));
            } else
                renderingExpression = entityClass.getForeignFields();
            String fields = dqlStatement.getFields();
            if (renderingExpression == null && fields != null)
                renderingExpression = entityClass.parseExpression(fields);
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
        if (dialogVisualGrid == null && entityRenderer != null) {
            dialogVisualGrid = new SkinnedVisualGrid(); // Better rendering in desktop JavaFx (but might be slower in web version)
            dialogVisualGrid.setHeaderVisible(false);
            dialogVisualGrid.setCursor(Cursor.HAND);
            BorderPane.setAlignment(dialogVisualGrid, Pos.TOP_LEFT);
            dialogVisualGrid.visualResultProperty().addListener((observable, oldValue, newValue) -> Platform.runLater(() -> deferredVisualResult.setValue(newValue)));
            EntityStore filterStore = loadingStore != null ? loadingStore : getSelectedItem() != null ? getSelectedItem().getStore() : null;
            entityDialogFilter = new ReactiveVisualFilter<E>(jsonOrClass)
                    .setDataSourceModel(dataSourceModel)
                    .setStore(filterStore)
                    .setRestrictedFilterList(restrictedFilterList)
                    .setEntityColumns(VisualEntityColumnFactory.get().create(renderingExpression))
                    .visualizeResultInto(dialogVisualGrid.visualResultProperty())
                    .setVisualSelectionProperty(dialogVisualGrid.visualSelectionProperty())
                    .setSelectedEntityHandler(dialogVisualGrid.visualSelectionProperty(), e -> {if (e != null && button != null) onDialogOk();})
            ;
            if (isSearchEnabled())
                entityDialogFilter
                    .combineIfNotEmpty(searchTextProperty(), s -> {
                        setSearchParameters(s, entityDialogFilter.getStore());
                        return where(searchCondition);
                    })
                    .combine(dialogHeightProperty(), height -> limit("?", updateAdaptiveLimit(height)));
            //dialogDataGrid.setOnMouseClicked(e -> {if (e.isPrimaryButtonDown() && e.getClickCount() == 1) onDialogOk(); });
        }
        return dialogVisualGrid;
    }

    private int updateAdaptiveLimit(Number height) {
        int maxNumberOfVisibleEntries = height.intValue() / 28;
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
        autoSelectFirstEntity(e -> true);
    }

    public void autoSelectFirstEntity(Predicate<E> predicate) {
        if (predicate == null)
            return;
        setUpDialog(false);
        if (entityDialogFilter != null)
            entityDialogFilter.setEntitiesHandler(entityList -> {
                setSelectedItem(entityList.stream().filter(predicate).findFirst().orElse(null));
                entityDialogFilter.setEntitiesHandler(null);
            });
    }

    public ReactiveVisualFilter<E> getEntityDialogFilter() {
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
