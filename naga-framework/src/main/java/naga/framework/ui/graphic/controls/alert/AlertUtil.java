package naga.framework.ui.graphic.controls.alert;

import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;

import static naga.framework.ui.layouts.LayoutUtil.setMaxSizeToInfinite;
import static naga.framework.ui.layouts.LayoutUtil.setMaxWidthToInfinite;

/**
 * @author Bruno Salmon
 */
public class AlertUtil {

    public static void showExceptionAlert(Throwable e, Window owner) {
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

        setMaxSizeToInfinite(textArea);
        GridPane.setVgrow(textArea, Priority.ALWAYS);
        GridPane.setHgrow(textArea, Priority.ALWAYS);

        GridPane expContent = setMaxWidthToInfinite(new GridPane());
        expContent.add(label, 0, 0);
        expContent.add(textArea, 0, 1);

// Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.show();
    }
}
