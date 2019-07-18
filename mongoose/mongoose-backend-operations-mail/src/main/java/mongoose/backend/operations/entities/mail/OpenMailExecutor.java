package mongoose.backend.operations.entities.mail;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Mail;
import webfx.platform.shared.util.async.Future;

final class OpenMailExecutor {

    static Future<Void> executeRequest(OpenMailRequest rq) {
        return execute(rq.getMail(), rq.getParentContainer());
    }

    private static Future<Void> execute(Mail mail, Pane parentContainer) {
        Future<Void> future = Future.future();
        return future;
    }
}
