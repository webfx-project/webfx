package mongoose.activities.shared.bookingform.shared;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;
import mongoose.activities.shared.logic.time.DayTimeRange;
import naga.commons.util.function.BiConsumer;
import naga.fx.spi.Toolkit;

/**
 * @author Bruno Salmon
 */
public class DayTimeRangeEditor {

    public interface DialogCallback {

        void closeDialog();

        void showException(Throwable e);

    }

    public static void showDayTimeRangeEditorDialog(DayTimeRange dayTimeRange, String headerText, BiConsumer<DayTimeRange, DialogCallback> okConsumer, Window ownerWindow) {
        String dayTimeRangeText = dayTimeRange.getText();
        TextInputDialog dialog = new TextInputDialog(dayTimeRangeText);
        dialog.initOwner(ownerWindow);
        dialog.setTitle("Editing day time range");
        dialog.setHeaderText(headerText);
        dialog.setContentText("Day time range:");
/*
                Property<Boolean> okDisabledProperty = dialog.getDialogPane().lookupButton(ButtonType.OK).disabledProperty();
                Properties.runNowAndOnPropertiesChange(p -> okDisabledProperty.setValue(dialog.getEditor().getText().equals(dialog.getDefaultValue())), dialog.getEditor().textProperty());
*/
        dialog.show();
        dialog.setOnCloseRequest(dialogEvent -> {
            String newDayTimeRangeText = dialog.getResult();
            if (newDayTimeRangeText == null /* Cancel button */ || newDayTimeRangeText.equals(dayTimeRangeText) /* Ok button but no change */)
                return; // No change => we return immediately and this will close the dialog
            // The user made a change, we don't close the dialog yet, we keep it open until the change is recorded in the database
            dialogEvent.consume(); // This will prevent the dialog to close
            okConsumer.accept(DayTimeRange.parse(newDayTimeRangeText), new DialogCallback() {
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
                        showExceptionAlert(e, dialog.getOwner());
                    });
                }
            });
        });
    }

    private static void showExceptionAlert(Throwable e, Window owner) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("Exception Dialog");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText(e.getMessage());

// Create expandable Exception.

        StringBuilder sb = new StringBuilder();
        for (StackTraceElement element : e.getStackTrace())
            sb.append(element).append('\n');
        String exceptionText = sb.toString();

        Label label = new Label("The exception stacktrace was:");

        Label textArea = new Label(exceptionText);
/*
        TextArea textArea = new TextArea(exceptionText);
        textArea.setEditable(false);
        textArea.setWrapText(true);
*/

        textArea.setMaxWidth(Double.MAX_VALUE);
        textArea.setMaxHeight(Double.MAX_VALUE);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = new GridPane();
        expContent.setMaxWidth(Double.MAX_VALUE);
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.show();
    }

}
