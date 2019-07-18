package mongoose.backend.operations.entities.documentline;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Document;
import webfx.platform.shared.util.async.Future;

final class AddNewDocumentLineExecutor {

    static Future<Void> executeRequest(AddNewDocumentLineRequest rq) {
        return execute(rq.getDocument(), rq.getParentContainer());
    }

    private static Future<Void> execute(Document documentLine, Pane parentContainer) {
        Future<Void> future = Future.future();
        return future;
    }
}