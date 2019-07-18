package mongoose.backend.operations.entities.mail;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Mail;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.platform.shared.util.async.AsyncFunction;

public final class OpenMailRequest implements HasOperationCode,
        HasOperationExecutor<OpenMailRequest, Void> {

    private final static String OPERATION_CODE = "OpenMail";

    private final Mail mail;
    private final Pane parentContainer;

    public OpenMailRequest(Mail mail, Pane parentContainer) {
        this.mail = mail;
        this.parentContainer = parentContainer;
    }

    Mail getMail() {
        return mail;
    }

    Pane getParentContainer() {
        return parentContainer;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<OpenMailRequest, Void> getOperationExecutor() {
        return OpenMailExecutor::executeRequest;
    }
}
