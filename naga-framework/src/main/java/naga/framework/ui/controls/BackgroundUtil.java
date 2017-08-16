package naga.framework.ui.controls;

import javafx.geometry.Insets;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.CornerRadii;
import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public class BackgroundUtil {

    public static Background newBackground(Paint fill) {
        return newBackground(fill, 0);
    }

    public static Background newBackground(Paint fill, double radius) {
        return newBackground(fill, radius, 0);
    }

    public static Background newBackground(Paint fill, double radius, double insets) {
        return new Background(new BackgroundFill(fill, radius <= 0 ? CornerRadii.EMPTY : new CornerRadii(radius), insets <= 0 ? Insets.EMPTY : new Insets(insets)));
    }

    public static Background newWebColorBackground(String webColor) {
        return newBackground(Color.web(webColor));
    }

    public static Background newLinearGradientBackground(String linearGradient) {
        return newLinearGradientBackground(linearGradient, 0);
    }

    public static Background newLinearGradientBackground(String linearGradient, double radius) {
        return newLinearGradientBackground(linearGradient, radius, 0);
    }

    public static Background newLinearGradientBackground(String linearGradient, double radius, double insets) {
        return newBackground(LinearGradient.valueOf(linearGradient), radius, insets);
    }

    public static Background newVerticalLinearGradientBackground(String topWebColor, String bottomWebColor, double radius) {
        return newVerticalLinearGradientBackground(topWebColor, bottomWebColor, radius, 0);
    }

    public static Background newVerticalLinearGradientBackground(String topWebColor, String bottomWebColor, double radius, double insets) {
        return newLinearGradientBackground("from 0% 0% to 0% 100%, " + topWebColor + " 0%, " + bottomWebColor + " 100%", radius, insets);
    }

    public static Background transparentBackground() {
        return newBackground(Color.TRANSPARENT);
    }
}
