package mongoose.backend.operations.entities.document.security;

import javafx.scene.layout.Pane;
import mongoose.backend.operations.entities.generic.SetEntityFieldRequest;
import mongoose.shared.entities.Document;

public final class ToggleMarkDocumentAsUncheckedRequest extends SetEntityFieldRequest {

    private final static String OPERATION_CODE = "ToggleMarkDocumentAsUnchecked";

    public ToggleMarkDocumentAsUncheckedRequest(Document document, Pane parentContainer) {
        super(document, "person_unknown,person_known,person_verified", "false,false,false", null, parentContainer);
    }

    @Override
    public Object getOperationCode() {
        return OPERATION_CODE;
    }
}
