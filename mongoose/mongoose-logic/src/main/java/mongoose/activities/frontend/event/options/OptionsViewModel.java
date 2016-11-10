package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingProcessViewModel;
import naga.toolkit.spi.nodes.GuiNode;
import naga.toolkit.spi.nodes.controls.Button;
import naga.toolkit.spi.nodes.controls.TextView;
import naga.toolkit.spi.nodes.layouts.VPage;

/**
 * @author Bruno Salmon
 */
class OptionsViewModel extends BookingProcessViewModel {

    private final VPage calendarPanel;
    private final TextView priceTextView;

    OptionsViewModel(GuiNode contentNode, VPage calendarPanel, TextView priceTextView, Button previousButton, Button nextButton) {
        super(contentNode, previousButton, nextButton);
        this.calendarPanel = calendarPanel;
        this.priceTextView = priceTextView;
    }

    VPage getCalendarPanel() {
        return calendarPanel;
    }

    TextView getPriceTextView() {
        return priceTextView;
    }
}
