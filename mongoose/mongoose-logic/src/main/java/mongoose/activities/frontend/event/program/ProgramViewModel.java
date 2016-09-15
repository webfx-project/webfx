package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.booking.BookingProcessViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;

/**
 * @author Bruno Salmon
 */
public class ProgramViewModel extends BookingProcessViewModel {

    public ProgramViewModel(GuiNode contentNode, Button previousButton) {
        super(contentNode, previousButton, null);
    }
}
