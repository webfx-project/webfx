package webfx.extras.materialdesign.util.background;

import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public final class BackgroundUtil {

    public final static Background TRANSPARENT_BACKGROUND = newBackground(Color.TRANSPARENT);

    public static Background newBackground(Paint fill) {
        return newBackground(fill, 0);
    }

    public static Background newBackground(Paint fill, double radius) {
        return newBackground(fill, radius, 0);
    }

    public static Background newBackground(Paint fill, double radius, double insets) {
        return newBackgroundBuilder(fill, radius, insets).build();
    }

    public static BackgroundBuilder newBackgroundBuilder(Paint fill, double radius, double insets) {
        return new BackgroundBuilder().setFillBuilder(newBackgroundFillBuilder(fill, radius, insets));
    }

    public static BackgroundFillBuilder newBackgroundFillBuilder(Paint fill, double radius, double insets) {
        return new BackgroundFillBuilder().setFill(fill).setRadius(radius).setInset(insets);
    }
}
