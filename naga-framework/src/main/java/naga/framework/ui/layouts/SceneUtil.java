package naga.framework.ui.layouts;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import naga.framework.ui.anim.Animations;
import naga.fx.properties.Properties;
import naga.fx.spi.Toolkit;
import naga.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public class SceneUtil {

    public static boolean isNodeVerticallyVisibleOnScene(Node node) {
        Bounds layoutBounds = node.getLayoutBounds();
        double minY = node.localToScene(0, layoutBounds.getMinY()).getY();
        double maxY = node.localToScene(0, layoutBounds.getMaxY()).getY();
        Scene scene = node.getScene();
        return minY >= 0 && maxY <= scene.getHeight();
    }

    public static boolean scrollNodeToBeVerticallyVisibleOnScene(Node node) {
        return scrollNodeToBeVerticallyVisibleOnScene(node, false, true);
    }

    public static boolean scrollNodeToBeVerticallyVisibleOnScene(Node node, boolean onlyIfNotVisible, boolean animate) {
        ScrollPane scrollPane = LayoutUtil.findScrollPaneAncestor(node);
        if (scrollPane != null && !(onlyIfNotVisible && isNodeVerticallyVisibleOnScene(node))) {
            double nodeTop = node.localToScene(0, 0).getY();
            double sceneHeight = node.getScene().getHeight();
            double delta = sceneHeight / 2 - nodeTop;
            double contentHeight = scrollPane.getContent().getLayoutBounds().getHeight();
            double viewportHeight = scrollPane.getViewportBounds().getHeight();
            double voffset = LayoutUtil.computeScrollPaneVoffset(scrollPane) - delta;
            double vValue = voffset / (contentHeight - viewportHeight);
            vValue = Math.max(0, Math.min(1, vValue));
            Animations.animateProperty(scrollPane.vvalueProperty(), vValue, animate);
            return true;
        }
        return false;
    }

    public static void autoFocusIfEnabled(Node node) {
        if (isAutoFocusEnabled())
            onSceneReady(node, scene -> node.requestFocus());
    }

    public static boolean isAutoFocusEnabled() {
        // TODO: make it a user setting that can be stored in the device
        // Default behaviour is to disable auto focus if this can cause a (probably unwanted) virtual keyboard to appear
        return !willAVirtualKeyboardAppearOnFocus();
    }

    public static boolean willAVirtualKeyboardAppearOnFocus() {
        // No API for this so temporary implementation based on screen width size
        Rectangle2D visualBounds = Toolkit.get().getPrimaryScreen().getVisualBounds();
        return Math.min(visualBounds.getWidth(), visualBounds.getHeight()) < 800;
    }

    public static void onSceneReady(Node node, Consumer<Scene> sceneConsumer) {
        onSceneReady(node.sceneProperty(), sceneConsumer);
    }

    public static void onSceneReady(ObservableValue<Scene> sceneProperty, Consumer<Scene> sceneConsumer) {
        Properties.onPropertySet(sceneProperty, sceneConsumer);
    }

    public static void installSceneFocusOwnerAutoScroll(Scene scene) {
        scene.focusOwnerProperty().addListener((observable, oldValue, newFocusOwner) ->
                scrollNodeToBeVerticallyVisibleOnScene(newFocusOwner, true, true)
        );
    }

    public static void installPrimarySceneFocusOwnerAutoScroll() {
        Toolkit.get().onReady(() -> onSceneReady(Toolkit.get().getPrimaryStage().sceneProperty(), SceneUtil::installSceneFocusOwnerAutoScroll));
    }

    public static void runOnceFocusIsOutside(Node node, Runnable runnable) {
        onSceneReady(node, scene -> {
            scene.focusOwnerProperty().addListener(new ChangeListener<Node>() {
                @Override
                public void changed(ObservableValue<? extends Node> observable, Node oldValue, Node newFocusOwner) {
                    if (!isFocusInside(node, newFocusOwner)) {
                        scene.focusOwnerProperty().removeListener(this);
                        runnable.run();
                    }
                }
            });
        });
    }

    public static boolean isFocusInside(Node node) {
        Scene scene = node.getScene();
        return scene != null && isFocusInside(node, scene.getFocusOwner());
    }

    private static boolean isFocusInside(Node node, Node focusOwner) {
        return hasAncestor(focusOwner, node);
    }

    private static boolean hasAncestor(Node node, Node parent) {
        while (true) {
            if (node == parent)
                return true;
            if (node == null)
                return false;
            node = node.getParent();
        }
    }
}
