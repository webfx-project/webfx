package naga.framework.ui.controls;

import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public class BorderUtil {

    public static Border newBorder(Paint fill) {
        return newBorder(fill, 0);
    }

    public static Border newBorder(Paint fill, double radius) {
        return newBorder(fill, radius, BorderStroke.THIN);
    }

    public static Border newBorder(Paint fill, double radius, double width) {
        return newBorder(fill, radius, new BorderWidths(width));
    }

    public static Border newBorder(Paint fill, double radius, BorderWidths widths) {
        return new Border(new BorderStroke(fill, BorderStrokeStyle.SOLID, radius <= 0 ? CornerRadii.EMPTY : new CornerRadii(radius), widths));
    }

    public static Border newWebColorBorder(String webColor) {
        return newBorder(Color.web(webColor));
    }

    public static Border newWebColorBorder(String webColor, double radius) {
        return newBorder(Color.web(webColor), radius);
    }

    public static Border newWebColorBorder(String webColor, double radius, double width) {
        return newBorder(Color.web(webColor), radius, width);
    }

    public static Border newWebColorBorder(String webColor, double radius, BorderWidths widths) {
        return newBorder(Color.web(webColor), radius, widths);
    }

    public static Border transparentBorder() {
        return newBorder(Color.TRANSPARENT);
    }
}
