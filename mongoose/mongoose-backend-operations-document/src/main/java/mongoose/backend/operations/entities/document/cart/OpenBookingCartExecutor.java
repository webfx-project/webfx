package mongoose.backend.operations.entities.document.cart;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Document;
import webfx.framework.client.ui.controls.alert.AlertUtil;
import webfx.kit.launcher.WebFxKitLauncher;
import webfx.platform.shared.util.async.Future;

final class OpenBookingCartExecutor {

    static Future<Void> executeRequest(OpenBookingCartRequest rq) {
        return execute(rq.getDocument(), rq.getParentContainer());
    }

    private static Future<Void> execute(Document document, Pane parentContainer) {
        Future<Void> future = Future.future();
        document.evaluateOnceLoaded("cartUrl").setHandler(ar -> {
            if (ar.failed())
                AlertUtil.showExceptionAlert(ar.cause(), parentContainer.getScene().getWindow());
            else
                WebFxKitLauncher.getApplication().getHostServices().showDocument(ar.result().toString());
        });
        return future;
    }
}
