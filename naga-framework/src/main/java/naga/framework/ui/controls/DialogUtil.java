package naga.framework.ui.controls;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.Point2D;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.*;
import naga.commons.util.collection.Collections;
import naga.commons.util.function.Consumer;
import naga.commons.util.tuples.Unit;
import naga.fx.properties.Properties;
import naga.fx.properties.Unregistrable;
import naga.fx.spi.Toolkit;

import java.util.ArrayList;
import java.util.List;

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
        return showModalNodeInGoldLayout(dialogBuilder, parent, 0, 0);
    }

    public static DialogCallback showModalNodeInGoldLayout(DialogBuilder dialogBuilder, Pane parent, double percentageWidth, double percentageHeight) {
        Region dialog = dialogBuilder.build();
        if (percentageWidth != 0)
            LayoutUtil.setPrefWidthToInfinite(dialog);
        if (percentageHeight != 0)
            LayoutUtil.setPrefHeightToInfinite(dialog);
        DialogCallback dialogCallback = showModalNodeInGoldLayout(dialog, parent, percentageWidth, percentageHeight);
        dialogBuilder.setDialogCallback(dialogCallback);
        return dialogCallback;
    }

    public static DialogCallback showModalNodeInGoldLayout(Region modalNode, Pane parent, double percentageWidth, double percentageHeight) {
        Insets padding = modalNode.getPadding();
        DialogCallback dialogCallback = showModalNode(LayoutUtil.createGoldLayout(decorate(modalNode), percentageWidth, percentageHeight), parent);
        dialogCallback.addCloseHook(() -> modalNode.setPadding(padding));
        return dialogCallback;
    }

    public static DialogCallback showModalNode(Region modalNode, Pane parent) {
        setMaxSizeToInfinite(modalNode).setManaged(false);
        DialogCallback dialogCallback = createDialogCallback(modalNode, parent);
        setUpModalNodeResizeRelocate(modalNode, parent, dialogCallback);
        return dialogCallback;
    }

    private static void setUpModalNodeResizeRelocate(Region modalNode, Pane parent, DialogCallback dialogCallback) {
        Scene scene = parent.getScene();
        if (scene == null) {
            parent.sceneProperty().addListener(new ChangeListener<Scene>() {
                @Override
                public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
                    observable.removeListener(this);
                    setUpModalNodeResizeRelocate(modalNode, parent, dialogCallback);
                }
            });
            return;
        }
        Unregistrable unregistrable = Properties.runNowAndOnPropertiesChange(p -> {
                Point2D parentSceneXY = parent.localToScene(0, 0);
                double width = Math.min(parent.getWidth(), scene.getWidth() - parentSceneXY.getX());
                double height = Math.min(parent.getHeight(), scene.getHeight() - parentSceneXY.getY());
                modalNode.resizeRelocate(0, 0, width, height);
            }, parent.widthProperty(), parent.heightProperty(), scene.widthProperty(), scene.heightProperty());
        dialogCallback.addCloseHook(unregistrable::unregister);
    }

    public static BorderPane decorate(Node content) {
        if (content instanceof Region) {
            Region region = (Region) content;
            // Setting max width/height to pref width/height (otherwise the grid pane takes all space with cells in top left corner)
            setMaxSizeToPref(region);
            region.setPadding(new Insets(30));
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

    public static DialogCallback showDropUpOrDownDialog(Region dialogNode, Region buttonNode, Pane parent, ObservableValue resizeProperty, boolean up) {
        DialogCallback dialogCallback = createDialogCallback(dialogNode, parent);
        dialogNode.setManaged(false);
        setUpDropDownDialogResizeRelocate(dialogNode, buttonNode, parent, dialogCallback, resizeProperty, up);
        return dialogCallback;
    }

    private static DialogCallback createDialogCallback(Region dialogNode, Pane parent) {
        parent.getChildren().add(dialogNode);
        return new DialogCallback() {
            private final List<Runnable> closeHooks = new ArrayList<>();
            @Override
            public void closeDialog() {
                Toolkit.get().scheduler().runInUiThread(() -> {
                    parent.getChildren().remove(dialogNode);
                    for (Runnable closeHook: closeHooks)
                        closeHook.run();
                });
            }

            @Override
            public void showException(Throwable e) {
                Toolkit.get().scheduler().runInUiThread(() -> AlertUtil.showExceptionAlert(e, parent.getScene().getWindow()));
            }

            @Override
            public void addCloseHook(Runnable closeHook) {
                closeHooks.add(closeHook);
            }
        };
    }

    private static void setUpDropDownDialogResizeRelocate(Region dialogNode, Region buttonNode, Pane parent, DialogCallback dialogCallback, ObservableValue resizeProperty, boolean up) {
        Scene scene = buttonNode.getScene();
        if (scene == null) {
            buttonNode.sceneProperty().addListener(new ChangeListener<Scene>() {
                @Override
                public void changed(ObservableValue<? extends Scene> observable, Scene oldValue, Scene newValue) {
                    observable.removeListener(this);
                    setUpDropDownDialogResizeRelocate(dialogNode, buttonNode, parent, dialogCallback, resizeProperty, up);
                }
            });
            return;
        }
        List<ObservableValue> reactingProperties = Collections.listOf(
                buttonNode.widthProperty(),
                buttonNode.heightProperty(),
                resizeProperty);
        for (ScrollPane scrollPane = findScrollPaneAncestor(buttonNode); scrollPane != null; scrollPane = findScrollPaneAncestor(scrollPane)) {
            reactingProperties.add(scrollPane.hvalueProperty());
            reactingProperties.add(scrollPane.vvalueProperty());
        }
        Unit<Runnable> runnableHolder = new Unit<>();
        Runnable runnable = () -> {
            Point2D buttonSceneXY = buttonNode.localToScene(0, 0);
            Point2D parentSceneXY = parent.localToScene(0, 0);
            double width = Math.min(buttonNode.getWidth(), scene.getWidth() - buttonSceneXY.getX());
            double height = dialogNode.prefHeight(width);
            double deltaY = up ? -height : buttonNode.getHeight();
            Region.layoutInArea(dialogNode, buttonSceneXY.getX() - parentSceneXY.getX(), buttonSceneXY.getY() - parentSceneXY.getY() + deltaY, width, height, -1, null, true, false, HPos.LEFT, VPos.TOP, false);
            // Hack for the html version which may have an incorrect height computation on first iteration
            if (height <= 32 && runnableHolder.get() != null) {
                javafx.application.Platform.runLater(runnableHolder.get());
                runnableHolder.set(null);
            }
        };
        runnable.run();
        runnableHolder.set(runnable);
        Unregistrable unregistrable = Properties.runOnPropertiesChange(p -> runnable.run(), Collections.toArray(reactingProperties, ObservableValue[]::new));
        dialogCallback.addCloseHook(unregistrable::unregister);
        dialogCallback.addCloseHook(() -> dialogNode.relocate(0, 0));
        scene.focusOwnerProperty().addListener(new ChangeListener<Node>() {
            @Override
            public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newFocusOwner) {
                if (!hasAncestor(newFocusOwner, dialogNode)) {
                    dialogCallback.closeDialog();
                    scene.focusOwnerProperty().removeListener(this);
                }
            }
        });
    }

    private static boolean hasAncestor(Node node, Parent parent) {
        while (true) {
            if (node == parent)
                return true;
            if (node == null)
                return false;
            node = node.getParent();
        }
    }

    private static ScrollPane findScrollPaneAncestor(Node node) {
        while (true) {
            if (node == null)
                return null;
            // Assuming ScrollPane has been created through LayoutUtil.createScrollPane() which stores the scrollPane into "parentScrollPane" node property
            ScrollPane parentScrollPane = (ScrollPane) node.getProperties().get("parentScrollPane");
            if (parentScrollPane != null)
                return parentScrollPane;
            node = node.getParent();
            if (node instanceof ScrollPane)
                return (ScrollPane) node;
        }
    }
}
