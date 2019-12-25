package mongoose.backend.operations.entities.allocationrule;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import webfx.framework.client.ui.controls.dialog.DialogContent;
import webfx.framework.client.ui.controls.dialog.DialogUtil;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.platform.shared.util.async.Future;

final class DeleteAllocationRuleExecutor {

    static Future<Void> executeRequest(DeleteAllocationRuleRequest rq) {
        return execute(rq.getDocumentLine(), rq.getParentContainer());
    }

    private static Future<Void> execute(Entity documentLine, Pane parentContainer) {
        Future<Void> future = Future.future();
        DialogContent dialogContent = new DialogContent().setContent(new Text("Are you sure you want to delete this rule?"));
        DialogUtil.showModalNodeInGoldLayout(dialogContent, parentContainer).addCloseHook(future::complete);
        DialogUtil.armDialogContentButtons(dialogContent, dialogCallback -> {
            UpdateStore updateStore = UpdateStore.create(documentLine.getStore().getDataSourceModel());
            updateStore.deleteEntity(documentLine);
            updateStore.submitChanges().setHandler(ar -> {
                if (ar.failed())
                    dialogCallback.showException(ar.cause());
                else
                    dialogCallback.closeDialog();
            });
        });
        return future;
    }
}
