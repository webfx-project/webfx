package mongoose.backend.operations.entities.document.registration;

import javafx.scene.layout.Pane;
import mongoose.backend.operations.entities.generic.SetEntityFieldRequest;
import mongoose.shared.entities.Document;

public final class ToggleConfirmDocumentRequest extends SetEntityFieldRequest {

    private final static String OPERATION_CODE = "ToggleConfirmDocument";

    public ToggleConfirmDocumentRequest(Document document, Pane parentContainer) {
        super(document, "confirmed,read", "!confirmed,passReady?false:true", "Are you sure you want to confirm this booking?", parentContainer);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }

}
