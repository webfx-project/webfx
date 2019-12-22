package mongoose.backend.operations.entities.document;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Document;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.platform.shared.util.async.AsyncFunction;

public final class SendLetterRequest implements HasOperationCode,
        HasOperationExecutor<SendLetterRequest, Void> {

    private final static String OPERATION_CODE = "SendLetter";

    private final Document document;
    private final Pane parentContainer;

    public SendLetterRequest(Document document, Pane parentContainer) {
        this.document = document;
        this.parentContainer = parentContainer;
    }

    Document getDocument() {
        return document;
    }

    Pane getParentContainer() {
        return parentContainer;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<SendLetterRequest, Void> getOperationExecutor() {
        return SendLetterExecutor::executeRequest;
    }
}
