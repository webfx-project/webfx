package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.booking.BookingProcessViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
class OptionsViewModel extends BookingProcessViewModel {

    public OptionsViewModel(GuiNode contentNode, Button previousButton, Button nextButton) {
        super(contentNode, previousButton, nextButton);
    }
}
