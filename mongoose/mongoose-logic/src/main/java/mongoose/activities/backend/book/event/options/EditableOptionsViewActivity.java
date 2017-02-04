package mongoose.activities.backend.book.event.options;

import javafx.beans.property.Property;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.layout.HBox;
import mongoose.activities.backend.book.event.shared.EditableBookingCalendar;
import mongoose.activities.shared.book.event.options.OptionsViewActivity;
import mongoose.activities.shared.book.event.shared.BookingCalendar;
import mongoose.entities.Option;
import naga.framework.orm.entity.UpdateStore;
import naga.framework.ui.controls.DialogContent;
import naga.framework.ui.controls.DialogUtil;

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
    protected List<Node> createOptionPanelHeaderNodes(Option option, Property<String> i18nTitle) {
        Button removeButton = getI18n().translateText(new Button(null, createImageView("{url: 'images/16/actions/remove.png', width: 16, height: 16}")), "Remove");
        removeButton.setOnAction(e -> DialogUtil.showDialog(
                DialogContent.createConfirmationDialog(
                        "Removing an option",
                        "Do you really want to remove this option?",
                        getI18n()),
                dialogCallback -> {
                    // Creating an update store
                    UpdateStore store = UpdateStore.create(getEventDataSourceModel());
                    // Creating an instance of Option entity
                    store.deleteEntity(option);
                    // Asking the update record this change in the database
                    store.executeUpdate().setHandler(asyncResult -> {
                        if (asyncResult.failed())
                            dialogCallback.showException(asyncResult.cause());
                        else {
                            dialogCallback.closeDialog();
                            // Updating the UI
                            getWorkingDocument().getWorkingDocumentLines().removeIf(line -> getTopParentOption(line.getOption()) == option);
                            getSelectedOptionsPreselection().getOptionPreselections().removeIf(optionPreselection -> getTopParentOption(optionPreselection.getOption()) == option);
                            getEventService().clearEventOptions();
                            startLogic();
                        }
                    });
                }, borderPane));

        List<Node> list = new ArrayList<>(super.createOptionPanelHeaderNodes(option, i18nTitle));
        Collections.addAll(list, createHGrowable(), setUnmanagedWhenInvisible(removeButton, editModeCheckBox.selectedProperty()));
        return list;
    }

    private Option getTopParentOption(Option option) {
        Option parent = option == null ? null : option.getParent();
        return parent == null ? option : getTopParentOption(parent);
    }
}
