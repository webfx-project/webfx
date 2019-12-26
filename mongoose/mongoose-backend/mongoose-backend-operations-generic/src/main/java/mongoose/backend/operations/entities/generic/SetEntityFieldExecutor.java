package mongoose.backend.operations.entities.generic;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import webfx.framework.client.ui.controls.alert.AlertUtil;
import webfx.framework.client.ui.controls.dialog.DialogCallback;
import webfx.framework.client.ui.controls.dialog.DialogContent;
import webfx.framework.client.ui.controls.dialog.DialogUtil;
import webfx.framework.shared.orm.expression.Expression;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.platform.shared.services.submit.SubmitArgument;
import webfx.platform.shared.util.async.Future;

final class SetEntityFieldExecutor {

    static Future<Void> executeRequest(SetEntityFieldRequest rq) {
        return execute(rq.getEntity(), rq.getLeftExpression(), rq.getRightExpression(), rq.getConfirmationText(), rq.getParentContainer());
    }

    private static Future<Void> execute(Entity entity, Expression<Entity> leftExpression, Expression<Entity> rightExpression, String confirmationText, Pane parentContainer) {
        Future<Void> future = Future.future();
        if (confirmationText == null)
            updateAndSave(entity, leftExpression, rightExpression, null, parentContainer);
        else {
            DialogContent dialogContent = new DialogContent().setContent(new Text(confirmationText));
            DialogUtil.showModalNodeInGoldLayout(dialogContent, parentContainer).addCloseHook(future::complete);
            DialogUtil.armDialogContentButtons(dialogContent, dialogCallback -> updateAndSave(entity, leftExpression, rightExpression, dialogCallback, parentContainer));
        }
        return future;
    }

    private static void updateAndSave(Entity entity, Expression<Entity> leftExpression, Expression<Entity> rightExpression, DialogCallback dialogCallback, Pane parentContainer) {
        entity.onExpressionLoaded(rightExpression).setHandler(ar1 -> {
            if (ar1.failed())
                reportException(dialogCallback, parentContainer, ar1.cause());
            else {
                UpdateStore updateStore = UpdateStore.createAbove(entity.getStore());
                Entity updateEntity = updateStore.updateEntity(entity);
                leftExpression.setValue(updateEntity, rightExpression.evaluate(updateEntity, updateStore.getEntityDataWriter()), updateStore.getEntityDataWriter());
                updateStore.submitChanges(SubmitArgument.builder()
                        .setStatement("select set_transaction_parameters(true)")
                        .setDataSourceId(entity.getStore().getDataSourceId())
                        .build()).setHandler(ar2 -> {
                            if (ar2.failed())
                                reportException(dialogCallback, parentContainer, ar2.cause());
                            else if (dialogCallback != null)
                                dialogCallback.closeDialog();
                        });
            }
        });
    }

    private static void reportException(DialogCallback dialogCallback, Pane parentContainer, Throwable cause) {
        if (dialogCallback != null)
            dialogCallback.showException(cause);
        else
            AlertUtil.showExceptionAlert(cause, parentContainer.getScene().getWindow());
    }
}
