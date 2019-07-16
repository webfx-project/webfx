package mongoose.backend.operations.entities.moneytransfer;

import javafx.scene.layout.Pane;
import mongoose.shared.entities.MoneyTransfer;
import webfx.framework.shared.operation.HasOperationCode;
import webfx.framework.shared.operation.HasOperationExecutor;
import webfx.platform.shared.util.async.AsyncFunction;

public final class EditPaymentRequest implements HasOperationCode,
        HasOperationExecutor<EditPaymentRequest, Void> {

    private final static String OPERATION_CODE = "EditPayment";

    private final MoneyTransfer payment;
    private final Pane parentContainer;

    public EditPaymentRequest(MoneyTransfer payment, Pane parentContainer) {
        this.payment = payment;
        this.parentContainer = parentContainer;
    }

    MoneyTransfer getPayment() {
        return payment;
    }

    Pane getParentContainer() {
        return parentContainer;
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

    @Override
    public AsyncFunction<EditPaymentRequest, Void> getOperationExecutor() {
        return EditPaymentExecutor::executeRequest;
    }
}
