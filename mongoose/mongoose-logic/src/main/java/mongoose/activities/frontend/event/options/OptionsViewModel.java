package mongoose.activities.frontend.event.options;

import mongoose.activities.frontend.event.shared.BookingProcessViewModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.text.Text;

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
