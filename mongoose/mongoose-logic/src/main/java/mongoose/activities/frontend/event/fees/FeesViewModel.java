package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.booking.BookingProcessViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class FeesViewModel extends BookingProcessViewModel {

    private final Button termsButton;
    private final Button programButton;

    public FeesViewModel(GuiNode contentNode, Button previousButton, Button nextButton, Button termsButton, Button programButton) {
        super(contentNode, previousButton, nextButton);
        this.termsButton = termsButton;
        this.programButton = programButton;
    }

    Button getTermsButton() {
        return termsButton;
    }

    Button getProgramButton() {
        return programButton;
    }
}
