package webfx.framework.client.ui.util.border;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public final class BorderUtil {

    public static Border transparentBorder() {
        return newBorder(Color.TRANSPARENT);
    }

    public static Border newBorder(Paint fill) {
        return newBorderBuilder(fill).build();
    }

    public static BorderBuilder newBorderBuilder(Paint fill) {
        return newBorderBuilder(newBorderStrokeBuilder(fill));
    }

    public static BorderBuilder newBorderBuilder(BorderStrokeBuilder borderStrokeBuilder) {
        return new BorderBuilder().setStrokeBuilder(borderStrokeBuilder);
    }

    public static BorderStrokeBuilder newBorderStrokeBuilder(Paint fill) {
        return new BorderStrokeBuilder().setStroke(fill);
    }

    public static Border newBorder(Paint fill, double radius) {
        return newBorderBuilder(fill, radius).build();
    }

    public static BorderBuilder newBorderBuilder(Paint fill, double radius) {
        return newBorderBuilder(newBorderStrokeBuilder(fill).setRadius(radius));
    }

    public static Border newBorder(Paint fill, double radius, double width) {
        return newBorderBuilder(fill, radius, width).build();
    }

    public static BorderBuilder newBorderBuilder(Paint fill, double radius, double width) {
        return newBorderBuilder(newBorderStrokeBuilder(fill).setRadius(radius).setWidth(width));
    }

    public static Border newWebColorBorder(String webColor, double radius) {
        return newBorderBuilder(Color.web(webColor), radius).build();
    }
}
