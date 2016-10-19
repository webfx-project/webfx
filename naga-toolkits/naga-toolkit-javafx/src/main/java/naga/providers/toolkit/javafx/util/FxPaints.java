package naga.providers.toolkit.javafx.util;

import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

/**
 * @author Bruno Salmon
 */
public class FxPaints {

    public static Paint toFxPaint(naga.toolkit.drawing.paint.Paint paint) {
        return toFxColor((naga.toolkit.drawing.paint.Color) paint);
    }

    public static  naga.toolkit.drawing.paint.Paint fromFxPaint(Paint paint) {
        return fromFxColor((Color) paint);
    }

    public static Color toFxColor(naga.toolkit.drawing.paint.Color color) {
        return new Color(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }

    public static  naga.toolkit.drawing.paint.Color fromFxColor(Color color) {
        return naga.toolkit.drawing.paint.Color.create(color.getRed(), color.getGreen(), color.getBlue(), color.getOpacity());
    }

}
