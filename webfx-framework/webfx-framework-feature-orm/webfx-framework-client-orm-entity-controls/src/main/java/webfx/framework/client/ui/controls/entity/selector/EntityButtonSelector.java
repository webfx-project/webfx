package webfx.framework.client.ui.controls.entity.selector;

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
import webfx.framework.client.orm.reactive.mapping.dql_to_entities.ReactiveEntitiesMapper;
import webfx.framework.client.ui.controls.button.ButtonFactoryMixin;
import webfx.framework.shared.orm.dql.DqlStatement;
import webfx.framework.shared.orm.dql.DqlStatementBuilder;
import webfx.framework.client.orm.reactive.mapping.entities_to_grid.EntityColumn;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.ReactiveVisualMapper;
import webfx.framework.client.orm.reactive.mapping.entities_to_visual.VisualEntityColumnFactory;
import webfx.framework.shared.orm.domainmodel.DataSourceModel;
import webfx.framework.shared.orm.domainmodel.DomainClass;
import webfx.framework.shared.orm.domainmodel.DomainModel;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.EntityList;
import webfx.framework.shared.orm.entity.EntityStore;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.expression.terms.ExpressionArray;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.async.Handler;
import webfx.platform.shared.util.function.Callable;

import java.util.List;
import java.util.function.Predicate;

import static webfx.framework.shared.orm.dql.DqlStatement.limit;
import static webfx.framework.shared.orm.dql.DqlStatement.where;

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
    private ReactiveVisualMapper<E> entityDialogMapper;
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
            entityDialogMapper = ReactiveVisualMapper.<E>createReactiveChain()
                    .always(jsonOrClass)
                    .setDataSourceModel(dataSourceModel)
                    .setStore(filterStore)
                    .setRestrictedFilterList(restrictedFilterList)
                    .setEntityColumns(VisualEntityColumnFactory.get().create(renderingExpression))
                    .visualizeResultInto(dialogVisualGrid)
                    .setSelectedEntityHandler(e -> {
                        if (e != null && button != null)
                            onDialogOk();
                    });
            if (isSearchEnabled())
                entityDialogMapper
                        .ifTrimNotEmpty(searchTextProperty(), s -> {
                            setSearchParameters(s, entityDialogMapper.getReactiveEntitiesMapper().getStore());
                            return where(searchCondition);
                        })
                        .always(dialogHeightProperty(), height -> limit("?", updateAdaptiveLimit(height)));
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
        if (entityDialogMapper != null) {
            ReactiveEntitiesMapper<E> reactiveEntitiesMapper = entityDialogMapper.getReactiveEntitiesMapper();
            Handler<EntityList<E>>[] entitiesHandlerHolder = new Handler[1];
            reactiveEntitiesMapper.addEntitiesHandler(entitiesHandlerHolder[0] = entityList -> {
                setSelectedItem(entityList.stream().filter(predicate).findFirst().orElse(null));
                reactiveEntitiesMapper.removeEntitiesHandler(entitiesHandlerHolder[0]);
            });
        }
    }

    public ReactiveVisualMapper<E> getEntityDialogMapper() {
        if (entityDialogMapper == null)
            getOrCreateDialogContent();
        return entityDialogMapper;
    }

    @Override
    protected void setUpDialog(boolean show) {
        super.setUpDialog(show);
        getEntityDialogMapper().start();
    }

    @Override
    protected void startLoading() {
        if (!getEntityDialogMapper().isStarted())
            entityDialogMapper.start();
    }

    @Override
    protected void onDialogOk() {
        setSelectedItem(getEntityDialogMapper().getSelectedEntity());
        super.onDialogOk();
    }

    @Override
    protected void closeDialog() {
        getEntityDialogMapper().stop();
        super.closeDialog();
    }
}
