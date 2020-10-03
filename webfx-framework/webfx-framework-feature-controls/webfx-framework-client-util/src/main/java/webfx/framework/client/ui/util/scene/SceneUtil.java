package webfx.framework.client.ui.util.scene;

import javafx.animation.Timeline;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.Property;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableMap;
import javafx.geometry.Bounds;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TextInputControl;
import javafx.stage.Window;
import webfx.framework.client.ui.util.anim.Animations;
import webfx.framework.client.ui.util.layout.LayoutUtil;
import webfx.kit.launcher.WebFxKitLauncher;
import webfx.kit.util.properties.Properties;
import webfx.kit.util.properties.Unregisterable;
import webfx.kit.util.properties.UnregisterableListener;
import webfx.platform.client.services.uischeduler.AnimationFramePass;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.services.scheduler.Scheduled;
import webfx.platform.shared.util.Booleans;
import webfx.platform.shared.util.tuples.Unit;

import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public final class SceneUtil {

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
        if (scrollPane != null && (!onlyIfNotVisible || !isNodeVerticallyVisibleOnScene(node))) {
            double contentHeight = scrollPane.getContent().getLayoutBounds().getHeight();
            double viewportHeight = scrollPane.getViewportBounds().getHeight();
            double nodeHeight = node.getLayoutBounds().getHeight();
            double sceneHeight = node.getScene().getHeight();
            VPos wishedPosition = getVerticalScrollNodeWishedPosition(node);
            double wishedSceneNodeTop = wishedPosition == VPos.TOP ? 0
                    : wishedPosition == VPos.BOTTOM ? sceneHeight - nodeHeight
                    : sceneHeight / 2 - nodeHeight / 2;
            double currentScrollPaneSceneTop = scrollPane.localToScene(0, 0).getY();
            wishedSceneNodeTop = LayoutUtil.boundedSize(wishedSceneNodeTop, currentScrollPaneSceneTop, currentScrollPaneSceneTop + viewportHeight);
            double currentNodeSceneTop = node.localToScene(0, 0).getY();
            double currentViewportSceneTop = LayoutUtil.computeScrollPaneVoffset(scrollPane);
            double wishedViewportSceneTop = currentViewportSceneTop +  currentNodeSceneTop - wishedSceneNodeTop;
            double vValue = wishedViewportSceneTop / (contentHeight - viewportHeight);
            vValue = LayoutUtil.boundedSize(vValue, 0, 1);
            Object timeline = scrollPane.getProperties().get("timeline");
            if (timeline instanceof Timeline)
                ((Timeline) timeline).stop();
            scrollPane.getProperties().put("timeline", Animations.animateProperty(scrollPane.vvalueProperty(), vValue, animate));
            return true;
        }
        return false;
    }

    public static void setVerticalScrollNodeWishedPosition(Node node, VPos wishedPosition) {
        node.getProperties().put("verticalScrollNodeWishedPosition", wishedPosition);
    }

    public static VPos getVerticalScrollNodeWishedPosition(Node node) {
        Object wishedPosition = node.getProperties().get("verticalScrollNodeWishedPosition");
        return wishedPosition instanceof VPos ? (VPos) wishedPosition : VPos.CENTER;
    }

    public static void autoFocusIfEnabled(Node node) {
        onSceneReady(node, scene -> {
            if (isAutoFocusEnabled(scene))
                node.requestFocus();
        });
    }

    public static boolean isAutoFocusEnabled(Scene scene) {
        // TODO: make it a user setting that can be stored in the device
        // Default behavior is to disable auto focus if this can cause a (probably unwanted) virtual keyboard to appear
        return isVirtualKeyboardShowing(scene) || !willAVirtualKeyboardAppearOnFocus(scene);
    }

    public static boolean willAVirtualKeyboardAppearOnFocus(Scene scene) {
        Boolean virtualKeyboardDetected = getSceneInfo(scene).virtualKeyboardDetected;
        if (virtualKeyboardDetected != null)
            return virtualKeyboardDetected;
        // No API for this so temporary implementation based on scene size
        return Math.min(scene.getWidth(), scene.getHeight()) < 800;
    }

    public static void onSceneReady(Node node, Consumer<Scene> sceneConsumer) {
        onSceneReady(node.sceneProperty(), sceneConsumer);
    }

    public static void onSceneReady(Window window, Consumer<Scene> sceneConsumer) {
        onSceneReady(window.sceneProperty(), sceneConsumer);
    }

    public static void onSceneReady(ObservableValue<Scene> sceneProperty, Consumer<Scene> sceneConsumer) {
        Properties.onPropertySet(sceneProperty, sceneConsumer);
    }

    public static void onPrimarySceneReady(Consumer<Scene> sceneConsumer) {
        WebFxKitLauncher.onReady(() -> onSceneReady(WebFxKitLauncher.getPrimaryStage(), sceneConsumer));
    }

    public static void installSceneFocusOwnerAutoScroll(Scene scene) {
        scene.focusOwnerProperty().addListener((observable, oldValue, newFocusOwner) -> {
            scrollNodeToBeVerticallyVisibleOnScene(newFocusOwner, true, true);
            if (newFocusOwner instanceof TextInputControl)
                getSceneInfo(scene).touchTextInputFocusTime();
        });
    }

    public static void installPrimarySceneFocusOwnerAutoScroll() {
        onPrimarySceneReady(SceneUtil::installSceneFocusOwnerAutoScroll);
    }

    public static boolean isVirtualKeyboardShowing(Scene scene) {
        return getSceneInfo(scene).isVirtualKeyboardShowing();
    }

    public static Unregisterable onVirtualKeyboardShowing(Scene scene, Runnable runnable) {
        return Properties.runOnPropertiesChange(p -> {
            if (Booleans.isTrue(p.getValue()))
                runnable.run();
        }, getSceneInfo(scene).virtualKeyboardShowingProperty);
    }

    public static Unregisterable runOnceFocusIsOutside(Node node, Runnable runnable) {
        Property<Node> localFocusOwnerProperty;
        ObservableValue<Node> focusOwnerProperty;
        Unit<Unregisterable> unregisterableUnit = new Unit<>();
        if (node.getScene() != null) {
            focusOwnerProperty = node.getScene().focusOwnerProperty();
            localFocusOwnerProperty = null;
        } else {
            focusOwnerProperty = localFocusOwnerProperty = new SimpleObjectProperty<>();
            onSceneReady(node, scene -> localFocusOwnerProperty.bind(scene.focusOwnerProperty()));
        }
        unregisterableUnit.set(new UnregisterableListener(p -> {
            if (!isFocusInside(node, (Node) p.getValue())) {
                runnable.run();
                unregisterableUnit.get().unregister();
            }
        }, focusOwnerProperty) {
            @Override
            public void unregister() {
                if (localFocusOwnerProperty != null)
                    localFocusOwnerProperty.unbind();
                super.unregister();
            }
        });
        return unregisterableUnit.get();
    }

    public static boolean isFocusInside(Node node) {
        Scene scene = node == null ? null : node.getScene();
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

    private static SceneInfo getSceneInfo(Scene scene) {
        ObservableMap<Object, Object> properties = scene.getProperties();
        Object p = properties.get("sceneInfo");
        if (!(p instanceof SceneInfo))
            properties.put("sceneInfo", p = new SceneInfo(scene));
        return (SceneInfo) p;
    }

    private final static long MAX_DELAY_MILLIS_BETWEEN_FOCUS_AND_VIRTUAL_KEYBOARD = 1000;

    private static final class SceneInfo {
        private Boolean virtualKeyboardDetected; // null = don't know yet, true = yes we detected it, no = we detected it was not here
        private final BooleanProperty virtualKeyboardShowingProperty = new SimpleBooleanProperty();
        private long lastTextInputFocusTime;
        private Scheduled noVirtualKeyboardDetectionScheduled;

        SceneInfo(Scene scene) {
            scene.heightProperty().addListener((observable, oldValue, newHeight) -> {
                boolean showing = lastTextInputFocusTime > 0 && System.currentTimeMillis() < lastTextInputFocusTime + MAX_DELAY_MILLIS_BETWEEN_FOCUS_AND_VIRTUAL_KEYBOARD;
                if (showing) {
                    cancelLastNoVirtualKeyboardDetection();
                    virtualKeyboardDetected = true;
                }
                virtualKeyboardShowingProperty.setValue(showing);
                // Also keeping focused text input control visible on screen when changing height
                UiScheduler.scheduleInAnimationFrame(() -> {
                    Node focusOwner = scene.getFocusOwner();
                    if (focusOwner instanceof TextInputControl)
                        scrollNodeToBeVerticallyVisibleOnScene(focusOwner, true, true);
                }, 2, AnimationFramePass.SCENE_PULSE_LAYOUT_PASS);
            });
        }

        void touchTextInputFocusTime() {
            lastTextInputFocusTime = System.currentTimeMillis();
            cancelLastNoVirtualKeyboardDetection();
            if (!isVirtualKeyboardShowing())
                noVirtualKeyboardDetectionScheduled = UiScheduler.scheduleDelay(MAX_DELAY_MILLIS_BETWEEN_FOCUS_AND_VIRTUAL_KEYBOARD, () -> virtualKeyboardDetected = false);
        }

        public boolean isVirtualKeyboardShowing() {
            return virtualKeyboardShowingProperty.get();
        }

        void cancelLastNoVirtualKeyboardDetection() {
            if (noVirtualKeyboardDetectionScheduled != null)
                noVirtualKeyboardDetectionScheduled.cancel();
            noVirtualKeyboardDetectionScheduled = null;
        }
    }
}
