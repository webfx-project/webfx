package naga.framework.ui.graphic.controls.sheet;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import naga.framework.orm.domainmodel.DomainClass;
import naga.framework.orm.entity.Entity;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.ui.filter.ExpressionColumn;
import naga.framework.ui.graphic.controls.button.ButtonFactoryMixin;
import naga.framework.ui.graphic.controls.dialog.DialogContent;
import naga.framework.ui.graphic.controls.dialog.DialogUtil;
import naga.framework.ui.graphic.materialdesign.MaterialFactoryMixin;
import naga.fxdata.cell.renderer.TextRenderer;
import naga.fxdata.cell.renderer.ValueRenderer;
import naga.fxdata.cell.renderer.ValueRenderingContext;
import naga.util.Arrays;

/**
 * @author Bruno Salmon
 */
public final class PropertySheet implements MaterialFactoryMixin, ButtonFactoryMixin {

    private final ExpressionColumn[] expressionColumns;
    private final ValueRenderingContext[] valueRenderingContexts;
    private Entity entity;
    private Region node;
    private UpdateStore updateStore;
    private Pane dialogParent; // Referenced only for possible transmission to the EntityRenderingContext

    public PropertySheet(ExpressionColumn[] expressionColumns) {
        this.expressionColumns = expressionColumns;
        valueRenderingContexts = Arrays.map(expressionColumns, this::getValueRenderingContext, ValueRenderingContext[]::new);
        // Temporary code
        TextRenderer.SINGLETON.setTextFieldFactory(this::newMaterialTextField);
    }

    public PropertySheet(Entity entity, String expressionColumns) {
        this(ExpressionColumn.fromJsonArrayOrExpressionsDefinition(expressionColumns, entity.getStore().getDomainModel(), entity.getDomainClass()));
        setEntity(entity);
    }

    private ValueRenderingContext getValueRenderingContext(ExpressionColumn expressionColumn) {
        String labelKey = expressionColumn.getDisplayColumn().getLabel().getCode();
        DomainClass foreignClass = expressionColumn.getForeignClass();
        // Returning a standard ValueRenderingContext if the expression expresses just a value and not a foreign entity
        if (foreignClass == null)
            return new ValueRenderingContext(false, labelKey, null);
        // Returning a EntityRenderingContext otherwise (in case of a foreign entity) which will be used by the EntityRenderer
        return new EntityRenderingContext(false, labelKey, null, expressionColumn, () -> entity.getStore(), () -> dialogParent, this);
    }

    public Object getEntity() {
        return entity;
    }

    public void setEntity(Entity entity) {
        this.entity = entity;
    }

    public Region getNode() {
        if (node == null)
            buildNode();
        return node;
    }

    private void buildNode() {
        node = new VBox(10);
        syncUiFromModel();
    }

    private void syncUiFromModel() {
        VBox vBox = (VBox) node;
        ObservableList<Node> children = vBox.getChildren();
        children.clear();
        for (int i = 0, n = expressionColumns.length; i < n; i++) {
            ExpressionColumn expressionColumn = expressionColumns[i];
            Object modelValue = entity.evaluate(expressionColumn.getExpression());
            // Using a standard value renderer in case of a value, or the EntityRenderer in case of a foreign entity
            ValueRenderer valueRenderer = expressionColumn.getForeignClass() == null ? expressionColumn.getDisplayColumn().getValueRenderer() : EntityRenderer.SINGLETON;
            children.add(valueRenderer.renderValue(modelValue, valueRenderingContexts[i]));
        }
    }

    private void syncModelFromUi() {
        updateStore = UpdateStore.createAbove(entity.getStore());
        Entity updateEntity = updateStore.updateEntity(entity);
        for (int i = 0, n = expressionColumns.length; i < n; i++) {
            ExpressionColumn expressionColumn = expressionColumns[i];
            Object uiValue = valueRenderingContexts[i].getEditedValueProperty().getValue();
            updateEntity.setExpressionValue(expressionColumn.getExpression(), uiValue);
        }
    }

    public void showAsDialog(Pane parent) {
        dialogParent = parent;
        DialogContent dialogContent = new DialogContent().setContent(getNode());
        DialogUtil.showModalNodeInGoldLayout(dialogContent, parent);
        DialogUtil.armDialogContentButtons(dialogContent, dialogCallback -> {
            syncModelFromUi();
            if (!updateStore.hasChanges())
                dialogCallback.closeDialog();
            else {
                updateStore.executeUpdate().setHandler(ar -> {
                    if (ar.failed())
                        dialogCallback.showException(ar.cause());
                    else
                        dialogCallback.closeDialog();
                });
            }
        });
    }

    public static void editEntity(Entity entity, String expressionColumns, Pane parent) {
        if (entity != null)
            new PropertySheet(entity, expressionColumns).showAsDialog(parent);
    }

}
