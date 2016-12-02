package naga.toolkit.fx.paint.impl;

import naga.toolkit.fx.paint.Color;

/**
 * @author Bruno Salmon
 */
public class ColorImpl implements Color {

    private double red;
    private double green;
    private double blue;
    private double opacity = 1;

    public ColorImpl(double red, double green, double blue, double opacity) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }

    @Override
    public double getRed() {
        return red;
    }

    @Override
    public double getGreen() {
        return green;
    }

    @Override
    public double getBlue() {
        return blue;
    }

    @Override
    public double getOpacity() {
        return opacity;
    }

    public static Color web(String colorString, double opacity) {
        if (colorString == null)
            throw new NullPointerException("The color components or name must be specified");

        String color = colorString.toLowerCase();
        if (color.startsWith("#"))
            color = color.substring(1);
        else if (color.startsWith("0x"))
            color = color.substring(2);
        else if (color.startsWith("rgb")) {
            if (color.startsWith("(", 3))
                return parseRGBColor(color, 4, false, opacity);
            if (color.startsWith("a(", 3))
                return parseRGBColor(color, 5, true, opacity);
        } else if (color.startsWith("hsl")) {
            if (color.startsWith("(", 3))
                return parseHSLColor(color, 4, false, opacity);
            if (color.startsWith("a(", 3))
                return parseHSLColor(color, 5, true, opacity);
        } else {
/*
            Color col = NamedColors.get(color);
            if (col != null) {
                if (opacity == 1.0) {
                    return col;
                } else {
                    return Color.color(col.red, col.green, col.blue, opacity);
                }
            }
*/
        }

        int len = color.length();

        try {
            int r;
            int g;
            int b;
            int a;

            if (len == 3) {
                r = Integer.parseInt(color.substring(0, 1), 16);
                g = Integer.parseInt(color.substring(1, 2), 16);
                b = Integer.parseInt(color.substring(2, 3), 16);
                return Color.rgba(r / 15.0, g / 15.0, b / 15.0, opacity);
            } else if (len == 4) {
                r = Integer.parseInt(color.substring(0, 1), 16);
                g = Integer.parseInt(color.substring(1, 2), 16);
                b = Integer.parseInt(color.substring(2, 3), 16);
                a = Integer.parseInt(color.substring(3, 4), 16);
                return Color.rgba(r / 15.0, g / 15.0, b / 15.0, opacity * a / 15.0);
            } else if (len == 6) {
                r = Integer.parseInt(color.substring(0, 2), 16);
                g = Integer.parseInt(color.substring(2, 4), 16);
                b = Integer.parseInt(color.substring(4, 6), 16);
                return Color.rgba255(r, g, b, opacity);
            } else if (len == 8) {
                r = Integer.parseInt(color.substring(0, 2), 16);
                g = Integer.parseInt(color.substring(2, 4), 16);
                b = Integer.parseInt(color.substring(4, 6), 16);
                a = Integer.parseInt(color.substring(6, 8), 16);
                return Color.rgba255(r, g, b, opacity * a / 255.0);
            }
        } catch (NumberFormatException nfe) {}

        throw new IllegalArgumentException("Invalid color specification");
    }

    /**
     * Creates an RGB color specified with an HTML or CSS attribute string.
     *
     * <p>
     * This method supports the following formats:
     * <ul>
     * <li>Any standard HTML color name
     * <li>An HTML long or short format hex string with an optional hex alpha
     * channel.
     * Hexadecimal values may be preceded by either {@code "0x"} or {@code "#"}
     * and can either be 2 digits in the range {@code 00} to {@code 0xFF} or a
     * single digit in the range {@code 0} to {@code F}.
     * <li>An {@code rgb(r,g,b)} or {@code rgba(r,g,b,a)} format string.
     * Each of the {@code r}, {@code g}, or {@code b} values can be an integer
     * from 0 to 255 or a floating point percentage value from 0.0 to 100.0
     * followed by the percent ({@code %}) character.
     * The alpha component, if present, is a
     * floating point value from 0.0 to 1.0.  Spaces are allowed before or
     * after the numbers and between the percentage number and its percent
     * sign ({@code %}).
     * <li>An {@code hsl(h,s,l)} or {@code hsla(h,s,l,a)} format string.
     * The {@code h} value is a floating point number from 0.0 to 360.0
     * representing the hue angle on a color wheel in degrees with
     * {@code 0.0} or {@code 360.0} representing red, {@code 120.0}
     * representing green, and {@code 240.0} representing blue.  The
     * {@code s} value is the saturation of the desired color represented
     * as a floating point percentage from gray ({@code 0.0}) to
     * the fully saturated color ({@code 100.0}) and the {@code l} value
     * is the desired lightness or brightness of the desired color represented
     * as a floating point percentage from black ({@code 0.0}) to the full
     * brightness of the color ({@code 100.0}).
     * The alpha component, if present, is a floating
     * point value from 0.0 to 1.0.  Spaces are allowed before or
     * after the numbers and between the percentage number and its percent
     * sign ({@code %}).
     * </ul>
     *
     * <p>Examples:</p>
     * <div class="classUseContainer">
     * <table class="overviewSummary" border="0" cellpadding="3" cellspacing="0">
     * <tr>
     * <th class="colFirst">Web Format String</th>
     * <th class="colLast">Equivalent constant or factory call</th>
     * </tr>
     * <tr class="rowColor">
     * <td class="colFirst"><code>Color.web("orange");</code></td>
     * <td class="colLast"><code>Color.ORANGE</code></td>
     * </tr>
     * <tr class="altColor">
     * <td class="colFirst"><code>Color.web("0xff668840");</code></td>
     * <td class="colLast"><code>Color.rgb(255, 102, 136, 0.25)</code></td>
     * </tr>
     * <tr class="rowColor">
     * <td class="colFirst"><code>Color.web("0xff6688");</code></td>
     * <td class="colLast"><code>Color.rgb(255, 102, 136, 1.0)</code></td>
     * </tr>
     * <tr class="altColor">
     * <td class="colFirst"><code>Color.web("#ff6688");</code></td>
     * <td class="colLast"><code>Color.rgb(255, 102, 136, 1.0)</code></td>
     * </tr>
     * <tr class="rowColor">
     * <td class="colFirst"><code>Color.web("#f68");</code></td>
     * <td class="colLast"><code>Color.rgb(255, 102, 136, 1.0)</code></td>
     * </tr>
     * <tr class="altColor">
     * <td class="colFirst"><code>Color.web("rgb(255,102,136)");</code></td>
     * <td class="colLast"><code>Color.rgb(255, 102, 136, 1.0)</code></td>
     * </tr>
     * <tr class="rowColor">
     * <td class="colFirst"><code>Color.web("rgb(100%,50%,50%)");</code></td>
     * <td class="colLast"><code>Color.rgb(255, 128, 128, 1.0)</code></td>
     * </tr>
     * <tr class="altColor">
     * <td class="colFirst"><code>Color.web("rgb(255,50%,50%,0.25)");</code></td>
     * <td class="colLast"><code>Color.rgb(255, 128, 128, 0.25)</code></td>
     * </tr>
     * <tr class="rowColor">
     * <td class="colFirst"><code>Color.web("hsl(240,100%,100%)");</code></td>
     * <td class="colLast"><code>Color.hsb(240.0, 1.0, 1.0, 1.0)</code></td>
     * </tr>
     * <tr class="altColor">
     * <td style="border-bottom:1px solid" class="colFirst">
     *     <code>Color.web("hsla(120,0%,0%,0.25)");</code>
     * </td>
     * <td style="border-bottom:1px solid" class="colLast">
     *     <code>Color.hsb(120.0, 0.0, 0.0, 0.25)</code>
     * </td>
     * </tr>
     * </table>
     * </div>
     *
     * @param colorString the name or numeric representation of the color
     *                    in one of the supported formats
     * @throws NullPointerException if {@code colorString} is {@code null}
     * @throws IllegalArgumentException if {@code colorString} specifies
     *      an unsupported color name or contains an illegal numeric value
     */
    public static Color web(String colorString) {
        return web(colorString, 1.0);
    }

    public static Color hsb(double hue, double saturation, double brightness, double opacity) {
        double[] rgb = HSBtoRGB(hue, saturation, brightness);
        return Color.rgba(rgb[0], rgb[1], rgb[2], opacity);
    }

    private static double[] HSBtoRGB(double hue, double saturation, double brightness) {
        // normalize the hue
        double normalizedHue = ((hue % 360) + 360) % 360;
        hue = normalizedHue/360;

        double r = 0, g = 0, b = 0;
        if (saturation == 0) {
            r = g = b = brightness;
        } else {
            double h = (hue - Math.floor(hue)) * 6.0;
            double f = h - Math.floor(h);
            double p = brightness * (1.0 - saturation);
            double q = brightness * (1.0 - saturation * f);
            double t = brightness * (1.0 - (saturation * (1.0 - f)));
            switch ((int) h) {
                case 0:
                    r = brightness;
                    g = t;
                    b = p;
                    break;
                case 1:
                    r = q;
                    g = brightness;
                    b = p;
                    break;
                case 2:
                    r = p;
                    g = brightness;
                    b = t;
                    break;
                case 3:
                    r = p;
                    g = q;
                    b = brightness;
                    break;
                case 4:
                    r = t;
                    g = p;
                    b = brightness;
                    break;
                case 5:
                    r = brightness;
                    g = p;
                    b = q;
                    break;
            }
        }
        double[] f = new double[3];
        f[0] = r;
        f[1] = g;
        f[2] = b;
        return f;
    }

    private static final int PARSE_COMPONENT = 0; // percent, or clamped to [0,255] => [0,1]
    private static final int PARSE_PERCENT = 1; // clamped to [0,100]% => [0,1]
    private static final int PARSE_ANGLE = 2; // clamped to [0,360]
    private static final int PARSE_ALPHA = 3; // clamped to [0.0,1.0]

    public static Color parseRGBColor(String color, int roff, boolean hasAlpha, double a) {
        try {
            int rend = color.indexOf(',', roff);
            int gend = rend < 0 ? -1 : color.indexOf(',', rend+1);
            int bend = gend < 0 ? -1 : color.indexOf(hasAlpha ? ',' : ')', gend+1);
            int aend = hasAlpha ? (bend < 0 ? -1 : color.indexOf(')', bend+1)) : bend;
            if (aend >= 0) {
                double r = parseComponent(color, roff, rend, PARSE_COMPONENT);
                double g = parseComponent(color, rend+1, gend, PARSE_COMPONENT);
                double b = parseComponent(color, gend+1, bend, PARSE_COMPONENT);
                if (hasAlpha) {
                    a *= parseComponent(color, bend+1, aend, PARSE_ALPHA);
                }
                return Color.rgba(r, g, b, a);
            }
        } catch (NumberFormatException nfe) {}

        throw new IllegalArgumentException("Invalid color specification");
    }

    public static Color parseHSLColor(String color, int hoff, boolean hasAlpha, double a) {
        try {
            int hend = color.indexOf(',', hoff);
            int send = hend < 0 ? -1 : color.indexOf(',', hend+1);
            int lend = send < 0 ? -1 : color.indexOf(hasAlpha ? ',' : ')', send+1);
            int aend = hasAlpha ? (lend < 0 ? -1 : color.indexOf(')', lend+1)) : lend;
            if (aend >= 0) {
                double h = parseComponent(color, hoff, hend, PARSE_ANGLE);
                double s = parseComponent(color, hend+1, send, PARSE_PERCENT);
                double l = parseComponent(color, send+1, lend, PARSE_PERCENT);
                if (hasAlpha) {
                    a *= parseComponent(color, lend+1, aend, PARSE_ALPHA);
                }
                return Color.hsba(h, s, l, a);
            }
        } catch (NumberFormatException nfe) {}

        throw new IllegalArgumentException("Invalid color specification");
    }

    private static double parseComponent(String color, int off, int end, int type) {
        color = color.substring(off, end).trim();
        if (color.endsWith("%")) {
            if (type > PARSE_PERCENT) {
                throw new IllegalArgumentException("Invalid color specification");
            }
            type = PARSE_PERCENT;
            color = color.substring(0, color.length()-1).trim();
        } else if (type == PARSE_PERCENT) {
            throw new IllegalArgumentException("Invalid color specification");
        }
        double c = ((type == PARSE_COMPONENT)
                ? Integer.parseInt(color)
                : Double.parseDouble(color));
        switch (type) {
            case PARSE_ALPHA:
                return (c < 0.0) ? 0.0 : ((c > 1.0) ? 1.0 : c);
            case PARSE_PERCENT:
                return (c <= 0.0) ? 0.0 : ((c >= 100.0) ? 1.0 : (c / 100.0));
            case PARSE_COMPONENT:
                return (c <= 0.0) ? 0.0 : ((c >= 255.0) ? 1.0 : (c / 255.0));
            case PARSE_ANGLE:
                return ((c < 0.0)
                        ? ((c % 360.0) + 360.0)
                        : ((c > 360.0)
                        ? (c % 360.0)
                        : c));
        }

        throw new IllegalArgumentException("Invalid color specification");
    }
}
