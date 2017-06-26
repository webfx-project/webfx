package naga.framework.ui.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.layout.*;
import naga.commons.util.function.Consumer;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;

import static naga.framework.ui.controls.LayoutUtil.setMaxSizeToInfinite;
import static naga.framework.ui.controls.LayoutUtil.setMaxSizeToPref;

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


    public static DialogCallback showModalNodeInGoldLayout(Region modalNode, Pane parent) {
        return showModalNodeInGoldLayout(modalNode, parent, 0, 0);
    }

    public static DialogCallback showModalNodeInGoldLayout(DialogBuilder dialogBuilder, Pane parent) {
        DialogCallback dialogCallback = showModalNodeInGoldLayout(dialogBuilder.build(), parent, 0, 0);
        dialogBuilder.setDialogCallback(dialogCallback);
        return dialogCallback;
    }

    public static DialogCallback showModalNodeInGoldLayout(Region modalNode, Pane parent, double percentageWidth, double percentageHeight) {
        return showModalNode(LayoutUtil.createGoldLayout(decorate(modalNode), percentageWidth, percentageHeight), parent);
    }

    public static DialogCallback showModalNode(Region modalNode, Pane parent) {
        setMaxSizeToInfinite(modalNode).setManaged(false);
        setUpModalNodeResize(modalNode, parent);
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

    private static void setUpModalNodeResize(Region modalNode, Pane parent) {
        Scene scene = parent.getScene();
        if (scene == null) {
            parent.sceneProperty().addListener(new ChangeListener<Scene>() {
                @Override
                public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
                    observable.removeListener(this);
                    setUpModalNodeResize(modalNode, parent);
                }
            });
            return;
        }
        Properties.runNowAndOnPropertiesChange(p -> {
            Point2D parentSceneXY = parent.localToScene(0, 0);
            double width = Math.min(parent.getWidth(), scene.getWidth() - parentSceneXY.getX());
            double height = Math.min(parent.getHeight(), scene.getHeight() - parentSceneXY.getY());
            modalNode.resizeRelocate(0, 0, width, height);
        }, parent.widthProperty(), parent.heightProperty(), scene.widthProperty(), scene.heightProperty());
    }

    public static BorderPane decorate(Node content) {
        if (content instanceof Region) {
            Region region = (Region) content;
            // Setting max width/height to pref width/height (otherwise the grid pane takes all space with cells in top left corner)
            setMaxSizeToPref(region);
            double padding = 30;
            region.setPadding(new Insets(padding));
        }
        BorderPane decorator = new BorderPane(content);
        decorator.backgroundProperty().bind(dialogBackgroundProperty());
        decorator.borderProperty().bind(dialogBorderProperty());
        decorator.setMinHeight(0d);
        return decorator;
    }

    public static void showDialog(DialogContent dialogContent, Consumer<DialogCallback> okConsumer, Pane parent) {
        DialogCallback dialogCallback = showModalNodeInGoldLayout(dialogContent.build(), parent);
        dialogContent.getCancelButton().setOnAction(event -> dialogCallback.closeDialog());
        dialogContent.getOkButton().setOnAction(event -> okConsumer.accept(dialogCallback));
    }

}
