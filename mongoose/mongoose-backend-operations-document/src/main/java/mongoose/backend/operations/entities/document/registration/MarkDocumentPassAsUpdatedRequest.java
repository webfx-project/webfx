package mongoose.backend.operations.entities.document.registration;

import javafx.scene.layout.Pane;
import mongoose.backend.operations.entities.generic.SetEntityFieldRequest;
import mongoose.shared.entities.Document;

public final class MarkDocumentPassAsUpdatedRequest extends SetEntityFieldRequest {

    private final static String OPERATION_CODE = "MarkDocumentPassAsUpdated";

    public MarkDocumentPassAsUpdatedRequest(Document document, Pane parentContainer) {
        super(document, "read", "true", null, parentContainer);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
