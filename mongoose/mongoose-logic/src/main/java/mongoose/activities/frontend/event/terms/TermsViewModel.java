package mongoose.activities.frontend.event.terms;

import mongoose.activities.frontend.event.booking.BookingProcessViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class TermsViewModel extends BookingProcessViewModel {

    public TermsViewModel(GuiNode contentNode, Button previousButton) {
        super(contentNode, previousButton, null);
    }
}
