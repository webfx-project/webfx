package mongoose.activities.backend.event.clone;

import naga.framework.ui.presentation.AbstractViewModel;
import naga.fx.scene.Node;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.Label;
import naga.fx.scene.control.TextField;

/**
 * @author Bruno Salmon
 */
class CloneEventViewModel extends AbstractViewModel {

    private final Label nameLabel;
    private final Label dateLabel;
    private final TextField nameTextField;
    private final TextField dateTextField;
    private final Button submitButton;

    CloneEventViewModel(Node contentNode, Label nameLabel, Label dateLabel, TextField nameTextField, TextField dateTextField, Button submitButton) {
        super(contentNode);
        this.nameLabel = nameLabel;
        this.dateLabel = dateLabel;
        this.nameTextField = nameTextField;
        this.dateTextField = dateTextField;
        this.submitButton = submitButton;
    }

    public Label getNameLabel() {
        return nameLabel;
    }

    public Label getDateLabel() {
        return dateLabel;
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
