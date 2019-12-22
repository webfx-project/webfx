package mongoose.backend.operations.entities.document.registration;

import javafx.scene.layout.Pane;
import mongoose.backend.operations.entities.generic.SetEntityFieldRequest;
import mongoose.shared.entities.Document;

public final class ToggleMarkDocumentPassAsReadyRequest extends SetEntityFieldRequest {

    private final static String OPERATION_CODE = "ToggleMarkDocumentPassAsReady";

    public ToggleMarkDocumentPassAsReadyRequest(Document document, Pane parentContainer) {
        super(document, "passReady,read", "!passReady,true", null, parentContainer);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
