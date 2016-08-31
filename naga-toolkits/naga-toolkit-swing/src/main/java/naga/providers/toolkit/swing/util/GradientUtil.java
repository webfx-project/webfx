package naga.providers.toolkit.swing.util;

import javax.swing.*;
import java.awt.*;

/**
 * @author Bruno Salmon
 */
class GradientUtil {

    static void paintVerticalGradient(JComponent c, Graphics g) {
        Color baseColor = c.getBackground();
        if (baseColor != Color.WHITE)
            paintGradient(c, g, createVerticalGradient(baseColor, c.getHeight()));
    }

    static void paintVerticalGradient(JComponent c, Color topColor, Color bottomColor, Graphics g) {
        paintGradient(c, g, createVerticalGradient(topColor, bottomColor, c.getHeight()));
    }

    static void paintGradient(JComponent c, Graphics g, GradientPaint vg) {
        Graphics2D g2d = (Graphics2D) g;
        g2d.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2d.setPaint(vg);
        g2d.fillRect(0, 0, c.getWidth(), c.getHeight());
    }

    private static GradientPaint createVerticalGradient(Color baseColor, int height) {
        return createVerticalGradient(baseColor, height, 0.9f);
    }

    private static GradientPaint createVerticalGradient(Color baseColor, int height, float factor) {
        Color topColor = baseColor;
        Color bottomColor = darker(topColor, factor);
        return createVerticalGradient(topColor, bottomColor, height);
    }

    private static GradientPaint createVerticalGradient(Color topColor, Color bottomColor, int height) {
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
