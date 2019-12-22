package mongoose.backend.operations.entities.document.registration;

import javafx.scene.layout.Pane;
import mongoose.backend.operations.entities.generic.ToggleBooleanEntityFieldRequest;
import mongoose.shared.entities.Document;

public final class ToggleMarkDocumentAsReadRequest extends ToggleBooleanEntityFieldRequest {

    private final static String OPERATION_CODE = "ToggleMarkDocumentAsRead";

    public ToggleMarkDocumentAsReadRequest(Document document, Pane parentContainer) {
        super(document, "read", null, parentContainer);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
