package mongoose.activities.shared.bookingform.shared;

import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import mongoose.activities.shared.logic.time.DayTimeRange;
import naga.commons.util.function.BiConsumer;
import naga.framework.ui.dialog.DialogUtil;
import naga.framework.ui.dialog.GridPaneBuilder;
import naga.framework.ui.i18n.I18n;
import naga.fx.spi.Toolkit;

import java.util.concurrent.TimeUnit;

/**
 * @author Bruno Salmon
 */
class DayTimeRangeEditor {

    static void showDayTimeRangeEditorDialog(DayTimeRange dayTimeRange, long epochDay, String headerText, BiConsumer<DayTimeRange, DialogUtil.DialogCallback> okConsumer, Node parentOwner, I18n i18n) {
        showDayTimeRangeInternDialog(dayTimeRange, epochDay, headerText, okConsumer, parentOwner, i18n);
    }

    private static void showDayTimeRangeInternDialog(DayTimeRange dayTimeRange, long epochDay, String headerText, BiConsumer<DayTimeRange, DialogUtil.DialogCallback> okConsumer, Node parentOwner, I18n i18n) {
        String generalText = dayTimeRange.getGeneralRule().getDayTimeSeries().toText();
        String exceptionText = dayTimeRange.getRuleForDay(epochDay, TimeUnit.DAYS).getDayTimeSeries().toText();

        TextField generalTextField = new TextField(generalText);
        TextField exceptionTextField = new TextField(exceptionText);
        Button okButton = new Button();
        Button cancelButton = new Button();

        DialogUtil.DialogCallback dialogCallback = DialogUtil.showModalNodeInGoldLayout(new GridPaneBuilder(i18n)
                        .addLabelTextFieldRow("GeneralRule", generalTextField)
                        .addLabelTextFieldRow("ExceptionRule", exceptionTextField)
                        .addButtons("Ok", okButton, "Cancel", cancelButton)
                        .getGridPane(),
                (Pane) parentOwner);

        okButton.setOnAction(e -> {
            okConsumer.accept(DayTimeRange.parse(generalTextField.getText()), dialogCallback);
        });
        cancelButton.setOnAction(e -> dialogCallback.closeDialog());
    }

    private static void showDayTimeRangeTextInputDialog(DayTimeRange dayTimeRange, String headerText, BiConsumer<DayTimeRange, DialogUtil.DialogCallback> okConsumer, Node parentOwner) {
        String dayTimeRangeText = dayTimeRange.getText();
        TextInputDialog dialog = new TextInputDialog(dayTimeRangeText);
        dialog.initOwner(parentOwner.getScene().getWindow());
        dialog.setTitle("Editing day time range");
        dialog.setHeaderText(headerText);
        dialog.setContentText("Day time range:");
        dialog.show();
        dialog.setOnCloseRequest(dialogEvent -> {
            String newDayTimeRangeText = dialog.getResult();
            if (newDayTimeRangeText == null /* Cancel button */ || newDayTimeRangeText.equals(dayTimeRangeText) /* Ok button but no change */)
                return; // No change => we return immediately and this will close the dialog
            // The user made a change, we don't close the dialog yet, we keep it open until the change is recorded in the database
            dialogEvent.consume(); // This will prevent the dialog to close
            okConsumer.accept(DayTimeRange.parse(newDayTimeRangeText), new DialogUtil.DialogCallback() {
                @Override
                public void closeDialog() {
                    Toolkit.get().scheduler().runInUiThread(() -> {
                        dialog.setResult(null); // Resetting result back to null for next pass
                        dialog.close();
                    });
                }

                @Override
                public void showException(Throwable e) {
                    Toolkit.get().scheduler().runInUiThread(() -> {
                        dialog.setResult(null); // Resetting result back to null for next pass
                        DialogUtil.showExceptionAlert(e, dialog.getOwner());
                    });
                }
            });
        });
    }

}
