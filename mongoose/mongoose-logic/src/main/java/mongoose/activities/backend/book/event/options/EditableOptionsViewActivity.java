package mongoose.activities.backend.book.event.options;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import mongoose.activities.backend.book.event.shared.EditableBookingCalendar;
import mongoose.activities.shared.book.event.options.OptionsViewActivity;
import mongoose.activities.shared.book.event.shared.BookingCalendar;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static naga.framework.ui.controls.LayoutUtil.createHGrowable;
import static naga.framework.ui.controls.LayoutUtil.setUnmanagedWhenInvisible;

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

    @Override
    protected List<Node> createItemFamilyPanelHeaderNodes(String familyCode, Property<String> i18nTitle) {
        List<Node> list = new ArrayList<>(super.createItemFamilyPanelHeaderNodes(familyCode, i18nTitle));
        Button removeButton = getI18n().translateText(new Button(null, createImageView("{url: 'images/16/actions/delete.png', width: 16, height: 16}")), "Remove");
        Collections.addAll(list, createHGrowable(), setUnmanagedWhenInvisible(removeButton, editModeCheckBox.selectedProperty()));
        return list;
    }
}
