package mongoose.activities.backend.event.clone;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
class CloneEventViewModel extends AbstractViewModel {

    private final Text nameText;
    private final Text dateText;
    private final TextField nameTextField;
    private final TextField dateTextField;
    private final Button submitButton;

    CloneEventViewModel(Node contentNode, Text nameText, Text dateText, TextField nameTextField, TextField dateTextField, Button submitButton) {
        super(contentNode);
        this.nameText = nameText;
        this.dateText = dateText;
        this.nameTextField = nameTextField;
        this.dateTextField = dateTextField;
        this.submitButton = submitButton;
    }

    public Text getNameText() {
        return nameText;
    }

    public Text getDateText() {
        return dateText;
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
