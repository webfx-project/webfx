package webfx.demos.mandelbrot.mandelbrotmodel;

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
public class Palette implements Cloneable {

    /**
     * The colorType for a palette in which colors are specified as Red/Green/Blue values.
     */
    public final static int COLOR_TYPE_RGB = 0;

    /**
     * The colorType for a palette in which colors are specified as Hue/Saturation/Brightness values.
     */
    public final static int COLOR_TYPE_HSB = 1;

    private int colorType;
    private boolean mirrorOutOfRangeComponents;

    private ArrayList<Double> divisionPoints;  // First element is always 0; last value is always 1
    private ArrayList<float[]> divisionPointColors; // Size is divisionPoints.size() + 1

/*
    private final ChangeEvent changeEvent = new ChangeEvent(this);
    private ArrayList<ChangeListener> changeListeners;
*/

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
        divisionPoints = new ArrayList<Double>();
        divisionPointColors = new ArrayList<float[]>();
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
     * Creates one of the built-in palettes used in the Mandelbrot program.
     * @param paletteName The name of the palette.  Must be one of "Spectrum",
     * "PaleSpectrum", "Grayscale", "CyclicGrayscale", "CyclicRedCyan",
     * "EarthSky", "HotCold", or "Fire".
     * @throws IllegalArgumentException if the parameter is not one of the valid
     * built-in palette names.
     */
    public static Palette makeDefaultPalette(String paletteName) {
        Palette palette;
        if (paletteName.equals("Spectrum")) {
            palette = new Palette();
        }
        else if (paletteName.equals("PaleSpectrum")) {
            palette = new Palette();
            palette.setDivisionPointColorComponents(0, 0, 0.5f, 1);
            palette.setDivisionPointColorComponents(1, 1, 0.5f, 1);
        }
        else if (paletteName.equals("DarkSpectrum")) {
            palette = new Palette();
            palette.setDivisionPointColorComponents(0, 0, 1, 0.5f);
            palette.setDivisionPointColorComponents(1, 1, 1, 0.5f);
        }
        else if (paletteName.equals("Grayscale")) {
            palette =new Palette(Palette.COLOR_TYPE_RGB);
        }
        else if (paletteName.equals("CyclicGrayscale")) {
            palette = new Palette(Palette.COLOR_TYPE_RGB);
            palette.split(0.5);
            palette.setDivisionPointColorComponents(1, 0, 0, 0);
            palette.setDivisionPointColorComponents(2, 1, 1, 1);
        }
        else if (paletteName.equals("CyclicRedCyan")) {
            palette = new Palette(Palette.COLOR_TYPE_RGB);
            palette.split(0.5);
            palette.setDivisionPointColorComponents(0, 1, 0, 0);
            palette.setDivisionPointColorComponents(1, 0, 1, 1);
            palette.setDivisionPointColorComponents(2, 1, 0, 0);
        }
        else if (paletteName.equals("EarthSky")) {
            palette = new Palette(Palette.COLOR_TYPE_RGB);
            palette.split(0.15);
            palette.split(0.33);
            palette.split(0.67);
            palette.split(0.85);
            palette.setDivisionPointColorComponents(0, 1, 1, 1);
            palette.setDivisionPointColorComponents(1, 1, 0.8f, 0);
            palette.setDivisionPointColorComponents(2, 0.53f, 0.12f, 0.075f);
            palette.setDivisionPointColorComponents(3, 0, 0, 0.6f);
            palette.setDivisionPointColorComponents(4, 0, 0.4f, 1);
            palette.setDivisionPointColorComponents(5, 1, 1, 1);
        }
        else if (paletteName.equals("HotCold")) {
            palette = new Palette(Palette.COLOR_TYPE_RGB);
            palette.split(0.16);
            palette.split(0.5);
            palette.split(0.84);
            palette.setDivisionPointColorComponents(0, 1, 1, 1);
            palette.setDivisionPointColorComponents(1, 0, 0.4f, 1);
            palette.setDivisionPointColorComponents(2, 0.2f, 0.2f, 0.2f);
            palette.setDivisionPointColorComponents(3, 1, 0, 0.8f);
            palette.setDivisionPointColorComponents(4, 1, 1, 1);
        }
        else if (paletteName.equals("Fire")) {
            palette = new Palette(Palette.COLOR_TYPE_RGB);
            palette.split(0.17);
            palette.split(0.83);
            palette.setDivisionPointColorComponents(0, 0, 0, 0);
            palette.setDivisionPointColorComponents(1, 1, 0, 0);
            palette.setDivisionPointColorComponents(2, 1, 1, 0);
            palette.setDivisionPointColorComponents(3, 1, 1, 1);
        }
        else
            throw new IllegalArgumentException("Unknown palette: " + paletteName);
        return palette;
    }

    public boolean equals(Object obj) {
        if (obj == null || !(obj instanceof Palette))
            return false;
        Palette that = (Palette)obj;
        if (that.colorType != colorType)
            return false;
        if (that.mirrorOutOfRangeComponents != mirrorOutOfRangeComponents)
            return false;
        if (that.divisionPoints.size() != divisionPoints.size())
            return false;
        for (int i = 0; i < divisionPoints.size(); i++) {
            if (that.divisionPoints.get(i) != divisionPoints.get(i))
                return false;
            float[] a = that.divisionPointColors.get(i);
            float[] b = divisionPointColors.get(i);
            if (a[0] != b[0] || a[1] != b[1] || a[2] != b[2])
                return false;
        }
        return true;
    }

    public Palette clone() {
        Palette that = new Palette(this.colorType);
        that.mirrorOutOfRangeComponents = this.mirrorOutOfRangeComponents;
        that.divisionPoints = new ArrayList<Double>();
        that.divisionPoints.addAll(this.divisionPoints);
        that.divisionPointColors = new ArrayList<float[]>();
        for (float[] c : this.divisionPointColors)
            that.divisionPointColors.add(cloneArray(c));
        //that.changed();
        return that;
    }

    private float[] cloneArray(float[] array) {
        //return array.clone(); // doesn't compile with GWT
        float[] array2 = new float[array.length];
        System.arraycopy(array, 0, array2, 0, array.length);
        return array2;
    }

    /**
     * Copies all properties from a specified palette, making this
     * palette equal to the specified palette.
     * @param that the palette whose properties are to be copied
     */
    public void copyFrom(Palette that) {
        this.colorType = that.colorType;
        this.mirrorOutOfRangeComponents = that.mirrorOutOfRangeComponents;
        this.divisionPoints = new ArrayList<Double>();
        this.divisionPoints.addAll(that.divisionPoints);
        this.divisionPointColors = new ArrayList<float[]>();
        for (float[] c : that.divisionPointColors)
            this.divisionPointColors.add(cloneArray(c));
        //this.changed();
    }

    /**
     * Removes a specified division point, one of the points where the color is specified explicitly.
     * @param divisionPointIndex the number of the division point to be removed, which much be
     * in the range 1 through ct-2, where ct is the number of division points.  It is not possible
     * to remove the first division point (which is 0.0) or the last division point (which is 1.0).
     */
    public void join(int divisionPointIndex) {
        if (divisionPointIndex <= 0 || divisionPointIndex >= divisionPoints.size() - 1)
            throw new IllegalArgumentException("Division point index out of range: " + divisionPointIndex);
        divisionPoints.remove(divisionPointIndex);
        divisionPointColors.remove(divisionPointIndex);
        //changed();
    }

    /**
     * Adds a division point to the palette.  The color associated to the point is obtained by
     * interpolating between the colors of the points that neighbor the new point.
     * @param divisionPoint The number between 0.0 and 1.0 where the new division point is to
     * be added.  The value cannot be the same as an existing point.
     * @return The index of the new division point, this is, its position number in the list of division points.
     * @throws IllegalArgumentException if the parameter is less than 0.0 or greater than 1.0, or if
     * a division point already exists at the specified value.
     */
    public int split(double divisionPoint) {  // Return value is index of divisionPoint/color that are inserted, or -1 if exact number already exists as a divisionPoint
        if (divisionPoint <= 0 || divisionPoint >= 1 || Double.isNaN(divisionPoint))
            throw new IllegalArgumentException("Division point out of range: " + divisionPoint);
        int index = 0;
        while (divisionPoint > divisionPoints.get(index))
            index++;
        if (Math.abs(divisionPoint - divisionPoints.get(index)) < 1e-15)
            return -1;
        float ratio = (float)( (divisionPoint - divisionPoints.get(index-1))
                / (divisionPoints.get(index) - divisionPoints.get(index-1)) );
        float[] c1 = divisionPointColors.get(index-1);
        float[] c2 = divisionPointColors.get(index);
        float a = c1[0] + ratio*(c2[0] - c1[0]);
        float b = c1[1] + ratio*(c2[1] - c1[1]);
        float c = c1[2] + ratio*(c2[2] - c1[2]);
        float[] color = new float[] { a, b, c };
        divisionPoints.add(index, divisionPoint);
        divisionPointColors.add(index, color);
        //changed();
        return index;
    }

    /**
     * Get the color that this palette assigns to a specified number.
     * @param position the number between 0.0 and 1.0, inclusive, for which the corresponding
     * color is to be returned.
     * @throws IllegalArgumentException if the position is outside the range 0.0 to 1.0.
     */
    public Color getColor(double position) {
        if (position < 0 || position > 1)
            throw new IllegalArgumentException("Position " + position + " is out of range.");
        int pt = 1;
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
        return color;
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
     * Returns the number of division points in the palette, always two or more.
     */
    public int getDivisionPointCount() {
        return divisionPoints.size();
    }

    /**
     * Returns a specified division points.  The return value is the range 0.0 to 1.0.  Divsion points
     * are stored in strictly increasing order.
     * @param index The index of the desired division point in the list of division points.
     */
    public double getDivisionPoint(int index) {
        return divisionPoints.get(index);
    }

    /**
     * Sets the value of a specified division point.  Does not apply to the first or last points,
     * which always have values 0.0 and 1.0.  The new value must be strictly between the positions
     * the neighboring division points.
     * @param index The index of the division point whose position is to be set.
     * @param position The new position for the specified division point.
     * @throws IllegalArgumentException if the index or position is not valid.
     */
    public void setDivisionPoint(int index, double position) {
        if (index <= 0 || index >= divisionPoints.size() - 1)
            throw new IllegalArgumentException("Index out of legal range");
        if (position <= divisionPoints.get(index-1) || position >= divisionPoints.get(index+1))
            throw new IllegalArgumentException("Division point position outside of legal range.");
        if (position != divisionPoints.get(index)) {
            divisionPoints.set(index, position);
            //changed();
        }
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

    /**
     * Returns the color components for the division point at a specified index.
     */
    public float[] getDivisionPointColorComponents(int index) {
        return cloneArray(divisionPointColors.get(index));
    }

    /**
     * Set the color components for the division point at a s specified index in the list of
     * division points.  These components are the color data that is stored for each division
     * point and that are used for interpolation between division points.  Note that when a color
     * is actually computed, the component values must be in the range 0.0 to 1.0.  However, the
     * values specified here do NOT have to be in this range.  Values given here are used for
     * interpolation, and then the resulting values are transformed into the range 0.0 to 1.0
     * just before the color is computed.  This means that the value can effectively oscillate
     * several times between two division points.
     * @param index
     * @param c1 The Red color component for an RGB palette, or the Hue component for an HSB palette.
     * @param c2 The Green color component for an RGB palette, or the Saturation component for an HSB palette.
     * @param c3 The Blue color component for an RGB palette, or the Brightness component for an HSB palette.
     */
    public void setDivisionPointColorComponents(int index, float c1, float c2, float c3) {
        float[] c = divisionPointColors.get(index);
        if (c1 == c[0] && c2 == c[1] && c3 == c[2])
            return;
        c[0] = c1;
        c[1] = c2;
        c[2] = c3;
        //changed();
    }

    /**
     * Return the color type of this palette, which is one of the constants Palette.COLOR_TYPE_RGB
     * or Palette.COLOR_TYPE_HSB.
     */
    public int getColorType() {
        return colorType;
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
        //changed();
    }

    /**
     * Add a listener that will be notified whenever the palette changes in any way.
     */
/*
    public void addChangeListener(ChangeListener listener) {
        if (listener == null)
            return;
        if (changeListeners == null)
            changeListeners = new ArrayList<ChangeListener>();
        if (!changeListeners.contains(listener))
            changeListeners.add(listener);
    }
*/

    /**
     * Remove a listener (if present in the list of listeners).
     */
/*
    public void removeChangeListener(ChangeListener listener) {
        if (changeListeners == null)
            return;
        changeListeners.remove(listener);
        if (changeListeners.size() == 0)
            changeListeners = null;
    }
*/

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

/*
    private void changed() {
        if (changeListeners != null)
            for (ChangeListener lst : changeListeners)
                lst.stateChanged(changeEvent);
    }
*/
}
