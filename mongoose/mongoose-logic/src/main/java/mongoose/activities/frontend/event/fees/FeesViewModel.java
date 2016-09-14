package mongoose.activities.frontend.event.fees;

import mongoose.activities.frontend.event.booking.BookingProcessViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class FeesViewModel extends BookingProcessViewModel {

    public FeesViewModel(GuiNode contentNode, Button previousButton, Button nextButton) {
        super(contentNode, previousButton, nextButton);
    }
}
