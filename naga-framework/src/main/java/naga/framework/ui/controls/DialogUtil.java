package naga.framework.ui.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.layout.*;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;

import static naga.framework.ui.controls.LayoutUtil.setMaxSizeToInfinite;
import static naga.framework.ui.controls.LayoutUtil.setMaxSizeToPref;

/**
 * @author Bruno Salmon
 */
public class DialogUtil {

    public interface DialogCallback {

        void closeDialog();

        void showException(Throwable e);

    }

    private final static Property<Background> dialogBackgroundProperty = new SimpleObjectProperty<>();
    public static Property<Background> dialogBackgroundProperty() {
        return dialogBackgroundProperty;
    }

    private final static Property<Border> dialogBorderProperty = new SimpleObjectProperty<>();
    public static Property<Border> dialogBorderProperty() {
        return dialogBorderProperty;
    }


    public static DialogCallback showModalNodeInGoldLayout(Region modalNode, Pane parent) {
        return showModalNode(LayoutUtil.createGoldLayout(decorate(modalNode)), parent);
    }

    public static DialogCallback showModalNode(Region modalNode, Pane parent) {
        setMaxSizeToInfinite(modalNode).setManaged(false);
        Properties.runNowAndOnPropertiesChange(p ->
                        modalNode.resizeRelocate(0, 0, parent.getWidth(), parent.getHeight()),
                parent.widthProperty(), parent.heightProperty());
        parent.getChildren().add(modalNode);
        return new DialogCallback() {
            @Override
            public void closeDialog() {
                Toolkit.get().scheduler().runInUiThread(() -> parent.getChildren().remove(modalNode));
            }

            @Override
            public void showException(Throwable e) {
                Toolkit.get().scheduler().runInUiThread(() -> AlertUtil.showExceptionAlert(e, parent.getScene().getWindow()));
            }
        };
    }

    public static BorderPane decorate(Node content) {
        if (content instanceof Region) {
            Region region = (Region) content;
            // Setting max width/height to pref width/height (otherwise the grid pane takes all space with cells in top left corner)
            setMaxSizeToPref(region);
            double padding = 30;
            region.setPadding(new Insets(padding, padding, padding, padding));
        }
        BorderPane decorator = new BorderPane(content);
        decorator.backgroundProperty().bind(dialogBackgroundProperty());
        decorator.borderProperty().bind(dialogBorderProperty());
        return decorator;
    }

}
