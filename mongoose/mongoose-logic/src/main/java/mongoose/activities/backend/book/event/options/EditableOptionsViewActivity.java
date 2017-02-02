package mongoose.activities.backend.book.event.options;

import javafx.scene.control.CheckBox;
import mongoose.activities.backend.book.event.shared.EditableBookingCalendar;
import mongoose.activities.shared.book.event.options.OptionsViewActivity;
import mongoose.activities.shared.book.event.shared.BookingCalendar;

/**
 * @author Bruno Salmon
 */
public class EditableOptionsViewActivity extends OptionsViewActivity {

    private CheckBox editModeCheckBox;

    @Override
    protected void createViewNodes() {
        super.createViewNodes();

    }

    @Override
    protected BookingCalendar createBookingCalendar() {
        return new EditableBookingCalendar(true, getI18n(), borderPane);
    }
}
