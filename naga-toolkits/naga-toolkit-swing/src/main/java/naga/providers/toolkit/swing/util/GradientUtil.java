package naga.providers.toolkit.swing.util;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
class GradientUtil {

    private static final Color SELECTION_COLOR = new Color(0x8A, 0xAA, 0xD3, 0x80);
    private static final Color TRANSPARENT_COLOR = new Color(0, 0, 0, 0);
    private static final LinearGradientPaint SELECTION_GRADIENT = new LinearGradientPaint(0, 0, 5, 10, new float[]{0.3f, 0.7f}, new Color[]{SELECTION_COLOR, TRANSPARENT_COLOR}, MultipleGradientPaint.CycleMethod.REFLECT);

    static void paintVerticalGradient(JComponent c, Graphics g, boolean selected) {
        Color baseColor = c.getBackground();
        if (baseColor == Color.WHITE || baseColor == null) {
            if (selected)
                paintGradient(c, g, (Paint) SELECTION_GRADIENT);
        } else if (selected)
            paintGradient(c, g, createVerticalGradient(baseColor, c.getHeight()), SELECTION_GRADIENT);
        else
            paintGradient(c, g, createVerticalGradient(baseColor, c.getHeight()));
    }

    static void paintVerticalGradient(JComponent c, Color topColor, Color bottomColor, Graphics g) {
        paintGradient(c, g, createVerticalGradient(topColor, bottomColor, c.getHeight()));
    }

    static void paintGradient(JComponent c, Graphics g, Paint... paints) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        for (Paint paint : paints) {
            g2d.setPaint(paint);
            g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
        }
    }

    private static Paint createVerticalGradient(Color baseColor, int height) {
        return createVerticalGradient(baseColor, height, 0.9f);
    }

    private static Paint createVerticalGradient(Color baseColor, int height, float factor) {
        Color topColor = baseColor;
        Color bottomColor = darker(topColor, factor);
        return createVerticalGradient(topColor, bottomColor, height);
    }

    private static Paint createVerticalGradient(Color topColor, Color bottomColor, int height) {
        return new GradientPaint(0, 0, topColor, 0, height, bottomColor);
    }

    private static Color darker(Color color, float factor) {
        return new Color(
                Math.max((int)(color.getRed()   * factor), 0),
                Math.max((int)(color.getGreen() * factor), 0),
                Math.max((int)(color.getBlue()  * factor), 0),
                color.getAlpha());
    }

    private static Color brighter(Color color, float FACTOR) {
        int r = color.getRed();
        int g = color.getGreen();
        int b = color.getBlue();
        int alpha = color.getAlpha();

        /* From 2D group:
         * 1. black.brighter() should return grey
         * 2. applying brighter to blue will always return blue, brighter
         * 3. non pure color (non zero rgb) will eventually return white
         */
        int i = (int)(1.0/(1.0-FACTOR));
        if ( r == 0 && g == 0 && b == 0) {
            return new Color(i, i, i, alpha);
        }
        if ( r > 0 && r < i ) r = i;
        if ( g > 0 && g < i ) g = i;
        if ( b > 0 && b < i ) b = i;

        return new Color(Math.min((int)(r/FACTOR), 255),
                Math.min((int)(g/FACTOR), 255),
                Math.min((int)(b/FACTOR), 255),
                alpha);
    }
}
