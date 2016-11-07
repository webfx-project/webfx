package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingProcessViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
class OptionsViewModel extends BookingProcessViewModel {

    private final VPage calendarPanel;

    OptionsViewModel(GuiNode contentNode, VPage calendarPanel, Button previousButton, Button nextButton) {
        super(contentNode, previousButton, nextButton);
        this.calendarPanel = calendarPanel;
    }

    VPage getCalendarPanel() {
        return calendarPanel;
    }
}
