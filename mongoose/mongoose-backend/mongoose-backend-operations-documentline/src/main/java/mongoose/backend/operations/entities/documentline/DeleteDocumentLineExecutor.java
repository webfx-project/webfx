package mongoose.backend.operations.entities.documentline;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import mongoose.shared.entities.DocumentLine;
import webfx.framework.client.ui.controls.dialog.DialogContent;
import webfx.framework.client.ui.controls.dialog.DialogUtil;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.platform.shared.util.async.Future;

final class DeleteDocumentLineExecutor {

    static Future<Void> executeRequest(DeleteDocumentLineRequest rq) {
        return execute(rq.getDocumentLine(), rq.getParentContainer());
    }

    private static Future<Void> execute(DocumentLine documentLine, Pane parentContainer) {
        Future<Void> future = Future.future();
        DialogContent dialogContent = new DialogContent().setContent(new Text("Are you sure you want to delete this option?"));
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
