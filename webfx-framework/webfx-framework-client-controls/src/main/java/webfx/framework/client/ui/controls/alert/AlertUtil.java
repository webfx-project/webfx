package webfx.framework.client.ui.controls.alert;

import javafx.scene.control.Label;
import javafx.scene.control.TextArea;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.stage.Window;
import webfx.framework.client.ui.controls.dialog.DialogContent;
import webfx.framework.client.ui.controls.dialog.DialogUtil;

import static webfx.framework.client.ui.util.layout.LayoutUtil.setMaxSizeToInfinite;
import static webfx.framework.client.ui.util.layout.LayoutUtil.setMaxWidthToInfinite;

/**
 * @author Bruno Salmon
 */
public final class AlertUtil {

    public static void showExceptionAlert(Throwable e, Window owner) {
        Label label = new Label("The exception stacktrace was:");
        StringBuilder sb = new StringBuilder(e.getMessage());
        for (StackTraceElement element : e.getStackTrace())
            sb.append('\n').append(element);
        String exceptionText = sb.toString();
        TextArea stackTraceArea = new TextArea(exceptionText);
        stackTraceArea.setEditable(false);
        stackTraceArea.setMinWidth(400);
        stackTraceArea.setMinHeight(300);
        //stackTraceArea.setWrapText(true); // Not implemented yet by WebFx
        setMaxSizeToInfinite(stackTraceArea);
        GridPane.setVgrow(stackTraceArea, Priority.ALWAYS);
        GridPane.setHgrow(stackTraceArea, Priority.ALWAYS);

        GridPane expContent = setMaxWidthToInfinite(new GridPane());
        expContent.add(label, 0, 0);
        expContent.add(stackTraceArea, 0, 1);

        DialogContent dialogContent = new DialogContent()
                .setTitle("An error occurred")
                //.setHeaderText(e.getMessage())
                .setContent(expContent);
        DialogUtil.showModalNodeInGoldLayout(dialogContent, (Pane) owner.getScene().getRoot());
        DialogUtil.armDialogContentButtons(dialogContent, null);

/* Version using Alert (not working yet with WebFx)
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.initOwner(owner);
        alert.setTitle("An error occurred");
        alert.setHeaderText("Look, an Exception Dialog");
        alert.setContentText(e.getMessage());
        // Set expandable Exception into the dialog pane.
        alert.getDialogPane().setExpandableContent(expContent);

        alert.show();
*/
    }
}
