package dev.webfx.kit.util.scene;

import dev.webfx.platform.os.OSFamily;
import dev.webfx.platform.os.OperatingSystem;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.paint.Paint;
import javafx.stage.Screen;

/**
 * @author Bruno Salmon
 */
public final class DeviceSceneUtil {

    public static Scene newScene(Parent root, double width, double height) {
        return newScene(root, getFinalSceneBounds(width, height));
    }

    public static Scene newScene(Parent root, double width, double height, Paint fill) {
        Scene scene = newScene(root, width, height);
        scene.setFill(fill); // Recommended to set a fill on the scene (ex: black) otherwise the Gluon version shows a white vertical line on the right side
        return scene;
    }

    public static Scene newScreenVisualBoundsScene(Parent root) {
        return newScene(root, getScreenVisualBounds());
    }

    public static Scene newScreenVisualBoundsScene(Parent root, Paint fill) {
        Scene scene = newScreenVisualBoundsScene(root);
        scene.setFill(fill); // Recommended to set a fill on the scene (ex: black) otherwise the Gluon version shows a white vertical line on the right side
        return scene;
    }

    public static Rectangle2D getFinalSceneBounds(double width, double height) {
        return isAndroidOrIOS() ? getScreenVisualBounds() // Also ok if actually runs in the browser as the scene dimensions will be ignored
                :  new Rectangle2D(0, 0, width, height);
    }

    public static Rectangle2D getSceneBoundsWithMaxPixelsCapOnDesktop(double maxPixels) {
        Rectangle2D screenVisualBounds = getScreenVisualBounds();
        if (isAndroidOrIOS())
            return screenVisualBounds;
        double w = screenVisualBounds.getWidth(), h = screenVisualBounds.getHeight(); // Trying full-screen
        double wh = w * h;
        if (wh <= maxPixels)
            return screenVisualBounds;
        double r = w / h;
        w = Math.sqrt(maxPixels * r);
        h = w / r;
        return new Rectangle2D(0, 0, w, h);
    }

    private static Rectangle2D getScreenVisualBounds() {
        return Screen.getPrimary().getVisualBounds();
    }

    private static Scene newScene(Parent root, Rectangle2D finalSceneBounds) {
        return new Scene(root, finalSceneBounds.getWidth(), finalSceneBounds.getHeight());
    }

    private static boolean isAndroidOrIOS() {
        OSFamily osFamily = OperatingSystem.getOSFamily();
        return osFamily == OSFamily.ANDROID || osFamily == OSFamily.IOS;
    }

}
