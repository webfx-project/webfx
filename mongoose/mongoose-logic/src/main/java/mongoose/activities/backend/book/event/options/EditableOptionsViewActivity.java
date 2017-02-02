package mongoose.activities.backend.book.event.options;

import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import mongoose.activities.backend.book.event.shared.EditableBookingCalendar;
import mongoose.activities.shared.book.event.options.OptionsViewActivity;
import mongoose.activities.shared.book.event.shared.BookingCalendar;

import static naga.framework.ui.controls.LayoutUtil.createHGrowable;

/**
 * @author Bruno Salmon
 */
public class EditableOptionsViewActivity extends OptionsViewActivity {

    private CheckBox editModeCheckBox;

    @Override
    protected void createViewNodes() {
        editModeCheckBox = getI18n().translateText(new CheckBox(), "EditMode");
        editModeCheckBox.setOnAction(event -> ((EditableBookingCalendar) bookingCalendar).setEditMode(editModeCheckBox.isSelected()));
        super.createViewNodes();
        borderPane.setBottom(new HBox(previousButton, createHGrowable(), editModeCheckBox, createHGrowable(), nextButton));
    }

    @Override
    protected BookingCalendar createBookingCalendar() {
        return new EditableBookingCalendar(true, getI18n(), borderPane);
    }
}
