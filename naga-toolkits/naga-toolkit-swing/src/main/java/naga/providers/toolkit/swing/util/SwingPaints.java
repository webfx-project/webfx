package naga.providers.toolkit.swing.util;


import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingPaints {

    public static Paint toSwingPaint(naga.toolkit.drawing.paint.Paint paint) {
        return toSwingColor((naga.toolkit.drawing.paint.Color) paint);
    }

    public static  naga.toolkit.drawing.paint.Paint fromSwingPaint(Paint paint) {
        return fromSwingColor((Color) paint);
    }

    public static Color toSwingColor(naga.toolkit.drawing.paint.Color color) {
        return new Color((float) color.getRed(), (float) color.getGreen(), (float) color.getBlue(), (float) color.getOpacity());
    }

    public static naga.toolkit.drawing.paint.Color fromSwingColor(Color color) {
        return naga.toolkit.drawing.paint.Color.create(color.getRed()*1f/255, color.getGreen()*1f/255, color.getBlue()*1f/255, color.getTransparency()*1f/255);
    }

}
