package mongoose.activities.backend.event.clone;

import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import naga.framework.ui.presentation.ViewModelBase;

/**
 * @author Bruno Salmon
 */
class CloneEventViewModel extends ViewModelBase {

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
