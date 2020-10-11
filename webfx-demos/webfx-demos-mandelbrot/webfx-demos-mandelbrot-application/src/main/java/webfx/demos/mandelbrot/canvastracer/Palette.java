package webfx.demos.mandelbrot.canvastracer;

import javafx.scene.paint.Color;

import java.util.ArrayList;

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
public class Palette {

    /**
     * The colorType for a palette in which colors are specified as Red/Green/Blue values.
     */
    public final static int COLOR_TYPE_RGB = 0;

    /**
     * The colorType for a palette in which colors are specified as Hue/Saturation/Brightness values.
     */
    public final static int COLOR_TYPE_HSB = 1;

    private final int colorType;
    private boolean mirrorOutOfRangeComponents;

    private final ArrayList<Double> divisionPoints;  // First element is always 0; last value is always 1
    private final ArrayList<float[]> divisionPointColors; // Size is divisionPoints.size() + 1

    /**
     * Creates a palette of HSB color type, showing a rainbow spectrum.
     */
    public Palette() {
        this(COLOR_TYPE_HSB);
    }

    /**
     * Create a palette of specified color type.  For HSB color type, the palette
     * is a rainbow spectrum.  For the RGB color type, the palette is a grayscale
     * from white to black.
     * @param colorType One of the constants Palette.COLOR_TYPE_HSB or Palette.COLOR_TYPE_RGB.
     * @throws IllegalArgumentException if the parameter is not one of the two valid color type constants.
     */
    public Palette(int colorType) {
        this.colorType = colorType;
        mirrorOutOfRangeComponents = true;
        divisionPoints = new ArrayList<>();
        divisionPointColors = new ArrayList<>();
        divisionPoints.add(0.0);
        divisionPoints.add(1.0);
        if (colorType == COLOR_TYPE_HSB) { // spectrum
            divisionPointColors.add(new float[] {0, 1, 1});
            divisionPointColors.add(new float[] {1, 1, 1});
        }
        else if (colorType == COLOR_TYPE_RGB){ // grayscale
            divisionPointColors.add(new float[] {1, 1, 1});
            divisionPointColors.add(new float[] {0, 0, 0});
        }
        else
            throw new IllegalArgumentException("Palette color type must be TYPE_COLOR_RGB or TYPE_COLOR_HSB");
    }

    Palette(int colorType, boolean mirrored, ArrayList<Double> divisionPoints, ArrayList<float[]> colorComponents) {
        // For use by the PaletteIO and PaletteEditDialog classes in the same package; no error checking done here.
        this.colorType = colorType;
        this.mirrorOutOfRangeComponents = mirrored;
        this.divisionPoints = divisionPoints;
        this.divisionPointColors = colorComponents;
    }

    /**
     * Get an array of RGB color values corresponding to equally spaced points in the
     * range 0.0 to 1.0.
     * @param paletteLength The number of points for which colors will be returned.
     * @param offset the color values are "rotated" by this amount within the array.
     * That is, the color value corresponding to 0.0 is in the array at index = offset
     * (or, more exactly, paletteLength % offset).
     */
    public Color[] makeRGBs(int paletteLength, int offset) {
        Color[] rgb;
        rgb = new Color[paletteLength];
        Color divisionPointColor = getDivisionPointColor(0);
        //int rgb1 = getRGB(divisionPointColor);
        rgb[offset % paletteLength] = divisionPointColor;
        int ct = 1;
        double dx = 1.0 / (paletteLength-1);
        int pt = 1;
        while (ct < paletteLength-1) {
            double position = dx*ct;
            while (position > divisionPoints.get(pt))
                pt++;
            float ratio = (float)( (position - divisionPoints.get(pt-1))
                    / (divisionPoints.get(pt) - divisionPoints.get(pt-1)) );
            float[] c1 = divisionPointColors.get(pt-1);
            float[] c2 = divisionPointColors.get(pt);
            float a = clamp1(c1[0] + ratio*(c2[0] - c1[0]));
            float b = clamp2(c1[1] + ratio*(c2[1] - c1[1]));
            float c = clamp2(c1[2] + ratio*(c2[2] - c1[2]));
            Color color;
            if (colorType == COLOR_TYPE_HSB)
                color = getHSBColor(a, b, c);
            else
                color = getRGBColor(a, b, c);
            //int rgb2 = getRGB(color);
            rgb[(ct + offset) % paletteLength] = color;
            ct++;
        }
        rgb[(offset + paletteLength-1) % paletteLength] = getDivisionPointColor(divisionPoints.size()-1);
        return rgb;
    }

    private Color getHSBColor(float h, float s, float b) {
        return Color.hsb(360 * h, s, b);
    }

    private Color getRGBColor(float r, float g, float b) {
        return Color.color(r, g, b);
    }

    /**
     * Get the color associated with a given division point.
     * @param index The index of the division point in the list of points.
     */
    public Color getDivisionPointColor(int index) {
        float[] components = divisionPointColors.get(index);
        float a = clamp1(components[0]);
        float b = clamp2(components[1]);
        float c = clamp2(components[2]);
        if (colorType == COLOR_TYPE_RGB)
            return getRGBColor(a,b,c);
        else
            return getHSBColor(a,b,c);
    }


    public boolean getMirrorOutOfRangeComponents() {
        return mirrorOutOfRangeComponents;
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
        if (this.mirrorOutOfRangeComponents == mirrorOutOfRangeComponents)
            return;
        this.mirrorOutOfRangeComponents = mirrorOutOfRangeComponents;
    }

    private float clamp1(float x) {
        if (colorType == COLOR_TYPE_HSB || !mirrorOutOfRangeComponents)
            return x - (float)Math.floor(x);
        else
            return clamp2(x);
    }

    private float clamp2(float x) {
        if (!mirrorOutOfRangeComponents)
            return x - (float)Math.floor(x);
        x = 2*(x/2 - (float)Math.floor(x/2));
        if (x > 1)
            x = 2 - x;
        return x;
    }
}
