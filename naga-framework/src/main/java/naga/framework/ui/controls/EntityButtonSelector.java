package naga.framework.ui.controls;

import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import naga.commons.util.Strings;
import naga.framework.activity.view.ViewActivityContextMixin;
import naga.framework.expression.Expression;
import naga.framework.orm.domainmodel.DataSourceModel;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.EntityStore;
import naga.framework.ui.filter.ExpressionColumn;
import naga.framework.ui.filter.ReactiveExpressionFilter;
import naga.framework.ui.filter.StringFilter;
import naga.framework.ui.filter.StringFilterBuilder;
import naga.framework.ui.i18n.I18n;
import naga.fxdata.cell.renderer.ValueRenderer;
import naga.fxdata.cell.renderer.ValueRendererFactory;
import naga.fxdata.control.DataGrid;

import static naga.framework.ui.controls.LayoutUtil.createHGrowable;
import static naga.framework.ui.controls.LayoutUtil.setPrefWidthToInfinite;

/**
 * @author Bruno Salmon
 */
public class EntityButtonSelector {

    private final Object jsonOrClass;
    private final ViewActivityContextMixin viewActivityContextMixin;
    private final Pane parent;

    private final DataSourceModel dataSourceModel;

    private Button entityButton;
    private Entity entity;
    private Expression entityExpression;
    private ValueRenderer entityRenderer;

    private EntityStore loadingStore;
    private BorderPane entityDialogPane;
    private TextField searchBox;
    private DialogCallback entityDialogCallback;
    private ReactiveExpressionFilter entityDialogFilter;

    public EntityButtonSelector(Object jsonOrClass, ViewActivityContextMixin viewActivityContextMixin, Pane parent, DataSourceModel dataSourceModel) {
        this.jsonOrClass = jsonOrClass;
        this.viewActivityContextMixin = viewActivityContextMixin;
        this.parent = parent;
        this.dataSourceModel = dataSourceModel;
        StringFilter stringFilter = new StringFilterBuilder(jsonOrClass).build();
        entityExpression = dataSourceModel.getDomainModel().getClass(stringFilter.getDomainClassId()).getForeignFields();
        entityRenderer = ValueRendererFactory.getDefault().createCellRenderer(entityExpression.getType());
    }

    public void setEditable(boolean editable) {
        getEntityButton().setDisable(!editable);
    }

    public void setLoadingStore(EntityStore loadingStore) {
        this.loadingStore = loadingStore;
    }

    public Entity getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
        updateEntityButton();
    }

    public Button getEntityButton() {
        if (entityButton == null)
            setEntityButton(new Button());
        return entityButton;
    }

    public void setEntityButton(Button entityButton) {
        this.entityButton = entityButton;
        entityButton.setOnAction(e -> showEntityDialog());

    }

    private void updateEntityButton() {
        getEntityButton().setGraphic(entityRenderer.renderCellValue(entity == null ? null : entity.evaluate(entityExpression)));
    }

    private void showEntityDialog() {
        if (entityDialogPane == null) {
            DataGrid dataGrid = new DataGrid();
            entityDialogPane = new BorderPane(setPrefWidthToInfinite(dataGrid));
            I18n i18n = viewActivityContextMixin.getI18n();
            EntityStore filterStore = loadingStore != null ? loadingStore : entity != null ? entity.getStore() : null;
            entityDialogFilter = new ReactiveExpressionFilter(jsonOrClass).setDataSourceModel(dataSourceModel).setI18n(i18n).setStore(filterStore);
            String searchCondition = entityDialogFilter.getDomainClass().getSearchCondition();
            if (searchCondition != null) {
                searchBox = i18n.translatePromptText(new TextField(), "GenericSearchPlaceholder");
                entityDialogPane.setTop(searchBox);
                entityDialogFilter.combine(searchBox.textProperty(), s -> {
                    if (Strings.isEmpty(s))
                        return null;
                    EntityStore store = entityDialogFilter.getStore();
                    store.setParameterValue("search", s);
                    store.setParameterValue("lowerSearch", s.toLowerCase());
                    store.setParameterValue("searchLike", "%" + s + "%");
                    store.setParameterValue("lowerSearchLike", "%" + s.toLowerCase() + "%");
                    return "{where: `" + searchCondition + "`}";
                });
            }
            entityDialogFilter
                    .setExpressionColumns(ExpressionColumn.create(entityExpression))
                    .displayResultSetInto(dataGrid.displayResultSetProperty())
                    .setDisplaySelectionProperty(dataGrid.displaySelectionProperty())
                    //.setSelectedEntityHandler(dataGrid.displaySelectionProperty(), o -> onOkEntityDialog())
                    .start();
            HBox hBox = new HBox(20, createHGrowable(), viewActivityContextMixin.newOkButton(this::onOkEntityDialog), viewActivityContextMixin.newCancelButton(this::onCancelEntityDialog), createHGrowable());
            hBox.setPadding(new Insets(20, 0, 0, 0));
            entityDialogPane.setBottom(hBox);
            dataGrid.setOnMouseClicked(e -> {if (e.getClickCount() == 1) onOkEntityDialog(); });
        }
        entityDialogFilter.setActive(true);
        entityDialogCallback = DialogUtil.showModalNodeInGoldLayout(entityDialogPane, parent, 0.9, 0.8);
        if (searchBox != null) {
            searchBox.setText(null); // Resetting the search box
            searchBox.requestFocus();
        }
    }

    private void onOkEntityDialog() {
        setEntity(entityDialogFilter.getSelectedEntity());
        closeEntityDialog();
    }

    private void onCancelEntityDialog() {
        closeEntityDialog();
    }

    private void closeEntityDialog() {
        entityDialogCallback.closeDialog();
        entityDialogFilter.setActive(false);
    }
    
}
