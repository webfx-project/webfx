package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.shared.BookingProcessViewModel;
import naga.fxdata.cell.collator.GridCollator;
import javafx.scene.Node;
import javafx.scene.control.Button;

/**
 * @author Bruno Salmon
 */
class FeesViewModel extends BookingProcessViewModel<Node> {

    private final GridCollator dateInfoCollator;
    private final Button termsButton;
    private final Button programButton;

    FeesViewModel(Node contentNode, GridCollator dateInfoCollator, Button previousButton, Button nextButton, Button termsButton, Button programButton) {
        super(contentNode, previousButton, nextButton);
        this.dateInfoCollator = dateInfoCollator;
        this.termsButton = termsButton;
        this.programButton = programButton;
    }

    Button getTermsButton() {
        return termsButton;
    }

    Button getProgramButton() {
        return programButton;
    }

    GridCollator getDateInfoCollator() {
        return dateInfoCollator;
    }
}
