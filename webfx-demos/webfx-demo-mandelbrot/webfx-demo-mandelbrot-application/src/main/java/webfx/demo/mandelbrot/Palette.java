package webfx.demo.mandelbrot;

import javafx.scene.paint.Color;
import javafx.scene.paint.LinearGradient;
import javafx.scene.paint.Stop;

import java.util.List;
import java.util.stream.Collectors;

/**
 * Represents a palette, that is a sequence of colors.  A palette assigns
 * a color to each real number in the range 0 through 1, inclusive.  The color
 * is specified at several points in this range (including at least 0 and 1).
 * These points are referred to as "division points."
 * Between these points, the color is determined by linear interpolation.  A palette
 * can have color type HSB or RGB.  For HSB colors, the interpolation is done in
 * the HSB color space; for RGB colors, the interpolation is done in the RGB color
 * space.
 */
final class Palette {

    /**
     * The colorType for a palette in which colors are specified as Red/Green/Blue values.
     */
    //final static int COLOR_TYPE_RGB = 0;

    /**
     * The colorType for a palette in which colors are specified as Hue/Saturation/Brightness values.
     */
    //final static int COLOR_TYPE_HSB = 1;

    private final boolean hsbInterpolation;
    private final LinearGradient linearGradient;
    private boolean mirrorOutOfRangeComponents;

    Palette(LinearGradient lg, boolean hsbInterpolation, boolean mirrored) {
        if (hsbInterpolation) {
            linearGradient = new LinearGradient(lg.getStartX(), lg.getStartY(), lg.getEndX(), lg.getEndY(), lg.isProportional(), lg.getCycleMethod(), lg.getStops().stream().map(s -> {
                Color c = s.getColor();
                double hue = c.getHue();
                if (hue == 0 && s == lg.getStops().get(lg.getStops().size() - 1))
                    hue = 360;
                return new Stop(s.getOffset(), Color.color(hue / 360, c.getSaturation(), c.getBrightness()));
            }).collect(Collectors.toList()));
        } else
            linearGradient = lg;
        this.mirrorOutOfRangeComponents = mirrored;
        this.hsbInterpolation = hsbInterpolation;
    }

    /**
     * Get an array of RGB color values corresponding to equally spaced points in the
     * range 0.0 to 1.0.
     * @param paletteLength The number of points for which colors will be returned.
     * @param offset the color values are "rotated" by this amount within the array.
     * That is, the color value corresponding to 0.0 is in the array at index = offset
     * (or, more exactly, paletteLength % offset).
     */
    Color[] computePaletteColors(int paletteLength, int offset) {
        Color[] rgb;
        rgb = new Color[paletteLength];
        Color divisionPointColor = getDivisionPointColor(0);
        rgb[offset % paletteLength] = divisionPointColor;
        int ct = 1;
        double dx = 1.0 / (paletteLength - 1);
        int pt = 1;
        List<Stop> stops = linearGradient.getStops();
        while (ct < paletteLength - 1) {
            double position = dx * ct;
            Stop s1 = null, s2;
            while (position > (s2 = stops.get(pt)).getOffset()) {
                s1 = s2;
                pt++;
            }
            if (s1 == null)
                s1 = stops.get(pt - 1);
            double ratio = (position - s1.getOffset())
                    / (s2.getOffset() - s1.getOffset());
            Color c1 = s1.getColor();
            Color c2 = s2.getColor();
            double red1 = c1.getRed(), green1 = c1.getGreen(), blue1 = c1.getBlue();
            double red2 = c2.getRed(), green2 = c2.getGreen(), blue2 = c2.getBlue();
            double red   = clamp1(red1   + ratio * (red2   - red1));
            double green = clamp2(green1 + ratio * (green2 - green1));
            double blue  = clamp2(blue1  + ratio * (blue2  - blue1));
            Color color;
            if (hsbInterpolation)
                color = getHSBColor(red, green, blue);
            else
                color = getRGBColor(red, green, blue);
            rgb[(ct + offset) % paletteLength] = color;
            ct++;
        }
        rgb[(offset + paletteLength-1) % paletteLength] = getDivisionPointColor(stops.size() - 1);
        return rgb;
    }

    private Color getHSBColor(double h, double s, double b) {
        return Color.hsb(360 * h, s, b);
    }

    private Color getRGBColor(double r, double g, double b) {
        return Color.color(r, g, b);
    }

    /**
     * Get the color associated with a given division point.
     * @param index The index of the division point in the list of points.
     */
    private Color getDivisionPointColor(int index) {
        Color color = linearGradient.getStops().get(index).getColor();
        double a = clamp1(color.getRed());
        double b = clamp2(color.getBlue());
        double c = clamp2(color.getGreen());
        if (hsbInterpolation)
            return getHSBColor(a,b,c);
        else
            return getRGBColor(a,b,c);
    }


    /**
     * Sets the value of the confusing mirrorOutOfRangeComponents property.  This only has
     * an effect when a floating-point color component value that is outside the range
     * 0.0 to 1.0 has to be transformed to a value within that range.  For a Hue, the whole-number
     * part is simply discarded.  For the other components however, the transformation depends
     * on the value of the mirrorOutOfRangeComponents property.  If the property is false,
     * the whole-number part is discarded, but this results in a discontinuity at integer values.
     * If the property is true, this discontinuity is avoided by having the value oscillate with
     * period 2 instead of cycle with period 1.  The default value is true, and this is never
     * changed in the Mandelbrot application.  So, really, you shouldn't even be reading this.
     */
    public void setMirrorOutOfRangeComponents(boolean mirrorOutOfRangeComponents) {
        this.mirrorOutOfRangeComponents = mirrorOutOfRangeComponents;
    }

    private double clamp1(double x) {
        if (hsbInterpolation || !mirrorOutOfRangeComponents)
            return x - Math.floor(x);
        else
            return clamp2(x);
    }

    private double clamp2(double x) {
        if (!mirrorOutOfRangeComponents)
            return x - Math.floor(x);
        x = 2 * (x / 2 - Math.floor(x / 2));
        if (x > 1)
            x = 2 - x;
        return x;
    }
}
