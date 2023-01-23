package dev.webfx.kit.util.scene;

import dev.webfx.platform.os.OperatingSystem;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Screen;

/**
 * @author Bruno Salmon
 */
public final class DeviceSceneUtil {

    public static Scene newScene(Parent root, double width, double height) {
        switch (OperatingSystem.getOSFamily()) {
            case ANDROID:
            case IOS:
                return newVisualBoundsScreenScene(root); // Also ok if actually runs in the browser as the scene dimensions will be ignored
            default:
                return new Scene(root, width, height);
        }
    }

    public static Scene newVisualBoundsScreenScene(Parent root) {
        Rectangle2D visualBounds = Screen.getPrimary().getVisualBounds();
        return new Scene(root, visualBounds.getWidth(), visualBounds.getHeight());
    }

}
