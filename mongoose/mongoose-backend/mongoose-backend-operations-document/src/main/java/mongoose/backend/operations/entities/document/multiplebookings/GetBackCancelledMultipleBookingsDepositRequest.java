package mongoose.backend.operations.entities.document.multiplebookings;

import javafx.scene.layout.Pane;
import mongoose.backend.operations.entities.generic.SetEntityFieldRequest;
import mongoose.shared.entities.Document;

public final class GetBackCancelledMultipleBookingsDepositRequest extends SetEntityFieldRequest {

    private final static String OPERATION_CODE = "GetBackCancelledMultipleBookingsDeposit";

    public GetBackCancelledMultipleBookingsDepositRequest(Document document, Pane parentContainer) {
        super(document, "triggerTransferFromOtherMultipleBookings", "true", "Please confirm", parentContainer);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
