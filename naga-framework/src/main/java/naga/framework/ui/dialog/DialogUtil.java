package naga.framework.ui.dialog;

import javafx.application.Platform;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Label;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.stage.Window;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;

import static javafx.scene.layout.Region.USE_PREF_SIZE;

/**
 * @author Bruno Salmon
 */
public class DialogUtil {

    private final static Property<Background> dialogBackgroundProperty = new SimpleObjectProperty<>();
    public static Property<Background> dialogBackgroundProperty() {
        return dialogBackgroundProperty;
    }

    private final static Property<Border> dialogBorderProperty = new SimpleObjectProperty<>();
    public static Property<Border> dialogBorderProperty() {
        return dialogBorderProperty;
    }


    public static GridPane createGoldLayout(Region child) {
        GridPane goldPane = new GridPane();
        goldPane.setAlignment(Pos.TOP_CENTER); // Horizontal alignment
        RowConstraints headerRowConstraints = new RowConstraints();
        headerRowConstraints.prefHeightProperty().bind(Properties.combine(goldPane.heightProperty(), child.heightProperty(),
                (gpHeight, cHeight) -> {
                    Platform.runLater(() -> goldPane.getRowConstraints().setAll(headerRowConstraints));
                    return (gpHeight.doubleValue() - cHeight.doubleValue()) / 2.61;
                }));
        child.setMaxWidth(USE_PREF_SIZE);
        child.setMaxHeight(USE_PREF_SIZE);
        goldPane.add(child, 0, 1);
        goldPane.setBackground(new Background(new BackgroundFill(Color.gray(0.3, 0.5), null, null)));
        return goldPane;
    }

    public static BorderPane decorate(Node content) {
        if (content instanceof Region) {
            Region region = (Region) content;
            // Setting max width/height to pref width/height (otherwise the grid pane takes all space with cells in top left corner)
            region.setMaxWidth(USE_PREF_SIZE);
            region.setMaxHeight(USE_PREF_SIZE);
            double padding = 30;
            region.setPadding(new Insets(padding, padding, padding, padding));
        }
        BorderPane decorator = new BorderPane(content);
        decorator.backgroundProperty().bind(dialogBackgroundProperty());
        decorator.borderProperty().bind(dialogBorderProperty());
        return decorator;
    }

    public static DialogCallback showModalNodeInGoldLayout(Region modalNode, Pane parent) {
        return showModalNode(createGoldLayout(decorate(modalNode)), parent);
    }

    public static DialogCallback showModalNode(Region modalNode, Pane parent) {
        modalNode.setManaged(false);
        modalNode.setMaxWidth(Double.MAX_VALUE);
        modalNode.setMaxHeight(Double.MAX_VALUE);
        Properties.runNowAndOnPropertiesChange(p ->
                        modalNode.resizeRelocate(0, 0, parent.getWidth(), parent.getHeight()),
                parent.widthProperty(), parent.heightProperty());
        parent.getChildren().add(modalNode);
        return new DialogCallback() {
            @Override
            public void closeDialog() {
                Toolkit.get().scheduler().runInUiThread(() -> {
                    parent.getChildren().remove(modalNode);
                });
            }

            @Override
            public void showException(Throwable e) {
                Toolkit.get().scheduler().runInUiThread(() -> {
                    showExceptionAlert(e, parent.getScene().getWindow());
                });
            }
        };
    }

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

    public interface DialogCallback {

        void closeDialog();

        void showException(Throwable e);

    }
}
