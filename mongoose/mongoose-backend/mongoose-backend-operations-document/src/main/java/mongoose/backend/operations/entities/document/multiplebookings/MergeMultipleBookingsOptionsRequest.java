package mongoose.backend.operations.entities.document.multiplebookings;

import javafx.scene.layout.Pane;
import mongoose.backend.operations.entities.generic.SetEntityFieldRequest;
import mongoose.shared.entities.Document;

public final class MergeMultipleBookingsOptionsRequest extends SetEntityFieldRequest {

    private final static String OPERATION_CODE = "MergeMultipleBookingsOptions";

    public MergeMultipleBookingsOptionsRequest(Document document, Pane parentContainer) {
        super(document, "triggerMergeFromOtherMultipleBookings", "true", "Please confirm", parentContainer);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
