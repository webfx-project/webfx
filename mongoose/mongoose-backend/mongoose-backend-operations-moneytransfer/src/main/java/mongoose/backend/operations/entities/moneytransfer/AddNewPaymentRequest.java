package mongoose.backend.operations.entities.moneytransfer;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.Document;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.platform.shared.util.async.AsyncFunction;

public final class AddNewPaymentRequest implements HasOperationCode,
        HasOperationExecutor<AddNewPaymentRequest, Void> {

    private final static String OPERATION_CODE = "AddNewPayment";

    private final Document document;
    private final Pane parentContainer;

    public AddNewPaymentRequest(Document document, Pane parentContainer) {
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
    public AsyncFunction<AddNewPaymentRequest, Void> getOperationExecutor() {
        return AddNewPaymentExecutor::executeRequest;
    }
}
