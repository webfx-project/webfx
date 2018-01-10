package naga.framework.ui.layouts;

import javafx.beans.value.ObservableValue;
import javafx.geometry.Bounds;
import javafx.geometry.Rectangle2D;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
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
            Properties.animateProperty(scrollPane.vvalueProperty(), vValue, animate);
            return true;
        }
        return false;
    }

    public static void autoFocusIfEnabled(Node node) {
        if (isAutoFocusEnabled())
            node.requestFocus();
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
        scene.focusOwnerProperty().addListener((observable, oldValue, newFocusOwner) -> scrollNodeToBeVerticallyVisibleOnScene(newFocusOwner, true, true));
    }

    public static void installPrimarySceneFocusOwnerAutoScroll() {
        Toolkit.get().onReady(() -> onSceneReady(Toolkit.get().getPrimaryStage().sceneProperty(), SceneUtil::installSceneFocusOwnerAutoScroll));
    }
}
