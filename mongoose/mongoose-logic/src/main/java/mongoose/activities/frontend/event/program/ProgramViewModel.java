package mongoose.activities.frontend.event.program;

import mongoose.activities.frontend.event.shared.BookingProcessViewModel;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;

/**
 * @author Bruno Salmon
 */
public class ProgramViewModel extends BookingProcessViewModel {

    private final BorderPane calendarPanel;
    private final BorderPane teachingsPanel;

    public ProgramViewModel(Node contentNode, BorderPane calendarPanel, BorderPane teachingsPanel, Button previousButton) {
        super(contentNode, previousButton, null);
        this.calendarPanel = calendarPanel;
        this.teachingsPanel = teachingsPanel;
    }

    public BorderPane getCalendarPanel() {
        return calendarPanel;
    }

    public BorderPane getTeachingsPanel() {
        return teachingsPanel;
    }
}
