package mongoose.backend.operations.entities.mail;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Document;
import webfx.platform.shared.util.async.Future;

final class ComposeNewMailExecutor {

    static Future<Void> executeRequest(ComposeNewMailRequest rq) {
        return execute(rq.getDocument(), rq.getParentContainer());
    }

    private static Future<Void> execute(Document document, Pane parentContainer) {
        Future<Void> future = Future.future();
        return future;
    }
}
