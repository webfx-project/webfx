package webfx.framework.client.ui.util.background;

import javafx.scene.layout.Background;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import webfx.framework.client.ui.util.paint.PaintBuilder;

/**
 * @author Bruno Salmon
 */
public final class BackgroundUtil {

    public final static Background TRANSPARENT_BACKGROUND = newBackground(Color.TRANSPARENT);
    public final static Background WHITE_BACKGROUND = newBackground(Color.WHITE);

    public static Background newWebColorBackground(String webColor) {
        return newBackground(Color.web(webColor));
    }

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

    public static BackgroundBuilder newBackgroundBuilder(PaintBuilder fillBuilder, double radius, double insets) {
        return new BackgroundBuilder().setFillBuilder(newBackgroundFillBuilder(fillBuilder, radius, insets));
    }

    public static BackgroundFillBuilder newBackgroundFillBuilder(PaintBuilder fillBuilder, double radius, double insets) {
        return new BackgroundFillBuilder().setFillBuilder(fillBuilder).setRadius(radius).setInset(insets);
    }

    public static Background newLinearGradientBackground(String linearGradient) {
        return newLinearGradientBackground(linearGradient, 0);
    }

    public static Background newLinearGradientBackground(String linearGradient, double radius) {
        return newLinearGradientBackground(linearGradient, radius, 0);
    }

    public static Background newLinearGradientBackground(String linearGradient, double radius, double insets) {
        return newBackgroundBuilder(new PaintBuilder().setLinearGradient(linearGradient), radius, insets).build();
    }

    public static Background newVerticalLinearGradientBackground(String topWebColor, String bottomWebColor, double radius) {
        return newVerticalLinearGradientBackground(topWebColor, bottomWebColor, radius, 0);
    }

    public static Background newVerticalLinearGradientBackground(String topWebColor, String bottomWebColor, double radius, double insets) {
        return newBackgroundBuilder(new PaintBuilder().setTopWebColor(topWebColor).setBottomWebColor(bottomWebColor), radius, insets).build();
    }
}
