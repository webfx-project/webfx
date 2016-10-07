package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.booking.BookingProcessViewModel;
import naga.toolkit.cell.collators.GridCollator;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class FeesViewModel extends BookingProcessViewModel {

    private final GridCollator dateInfoCollator;
    private final Button termsButton;
    private final Button programButton;

    public FeesViewModel(GuiNode contentNode, GridCollator dateInfoCollator, Button previousButton, Button nextButton, Button termsButton, Button programButton) {
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
