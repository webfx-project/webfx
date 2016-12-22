package mongoose.activities.backend.event.clone;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
class CloneEventViewModel extends AbstractViewModel {

    private final TextField nameTextField;
    private final TextField dateTextField;
    private final Button submitButton;

    CloneEventViewModel(Node contentNode, TextField nameTextField, TextField dateTextField, Button submitButton) {
        super(contentNode);
        this.nameTextField = nameTextField;
        this.dateTextField = dateTextField;
        this.submitButton = submitButton;
    }

    TextField getNameTextField() {
        return nameTextField;
    }

    TextField getDateTextField() {
        return dateTextField;
    }

    Button getSubmitButton() {
        return submitButton;
    }
}
