package mongoose.backend.operations.entities.resourceconfiguration;

import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import webfx.framework.client.ui.controls.dialog.DialogContent;
import webfx.framework.client.ui.controls.dialog.DialogUtil;
import webfx.framework.shared.orm.entity.Entity;
import webfx.framework.shared.orm.entity.UpdateStore;
import webfx.platform.shared.util.async.Future;

final class DeleteResourceExecutor {

    static Future<Void> executeRequest(DeleteResourceRequest rq) {
        return execute(rq.getResourceConfiguration(), rq.getParentContainer());
    }

    private static Future<Void> execute(Entity resourceConfiguration, Pane parentContainer) {
        Future<Void> future = Future.future();
        DialogContent dialogContent = new DialogContent().setContent(new Text("Are you sure you want to delete room " + resourceConfiguration.evaluate("name") + '?'));
        DialogUtil.showModalNodeInGoldLayout(dialogContent, parentContainer).addCloseHook(future::complete);
        DialogUtil.armDialogContentButtons(dialogContent, dialogCallback -> {
            UpdateStore updateStore = UpdateStore.create(resourceConfiguration.getStore().getDataSourceModel());
            updateStore.deleteEntity(resourceConfiguration);
            updateStore.deleteEntity(resourceConfiguration.<Entity>getForeignEntity("resource"));
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
