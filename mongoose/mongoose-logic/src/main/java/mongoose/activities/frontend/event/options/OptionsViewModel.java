package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingProcessViewModel;
import naga.fx.scene.Node;
import naga.fx.scene.control.Button;
import naga.fx.scene.layout.BorderPane;
import naga.fx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
class OptionsViewModel extends BookingProcessViewModel {

    private final BorderPane calendarPanel;
    private final Text priceText;

    OptionsViewModel(Node contentNode, BorderPane calendarPanel, Text priceText, Button previousButton, Button nextButton) {
        super(contentNode, previousButton, nextButton);
        this.calendarPanel = calendarPanel;
        this.priceText = priceText;
    }

    BorderPane getCalendarPanel() {
        return calendarPanel;
    }

    Text getPriceText() {
        return priceText;
    }
}
