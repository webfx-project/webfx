package naga.toolkit.fx.scene.paint;

/**
 * @author Bruno Salmon
 */
public class Color implements Paint {

    private double red;
    private double green;
    private double blue;
    private double opacity = 1;

    public Color(double red, double green, double blue, double opacity) {
        this.red = red;
        this.green = green;
        this.blue = blue;
        this.opacity = opacity;
    }

    /**
     * The red component of the {@code Color}, in the range {@code 0.0-1.0}.
     */
    public double getRed() {
        return red;
    }

    /**
     * The green component of the {@code Color}, in the range {@code 0.0-1.0}.
     */
    public double getGreen() {
        return green;
    }

    /**
     * The blue component of the {@code Color}, in the range {@code 0.0-1.0}.
     */
    public double getBlue() {
        return blue;
    }

    /**
     * The opacity of the {@code Color}, in the range {@code 0.0-1.0}.
     */
    public double getOpacity() {
        return opacity;
    }

    public boolean isOpaque() {
        return getOpacity() >= 1f;
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
     * <td class="colLast"><code>rgb(255, 102, 136, 0.25)</code></td>
     * </tr>
     * <tr class="rowColor">
     * <td class="colFirst"><code>Color.web("0xff6688");</code></td>
     * <td class="colLast"><code>rgb(255, 102, 136, 1.0)</code></td>
     * </tr>
     * <tr class="altColor">
     * <td class="colFirst"><code>Color.web("#ff6688");</code></td>
     * <td class="colLast"><code>rgb(255, 102, 136, 1.0)</code></td>
     * </tr>
     * <tr class="rowColor">
     * <td class="colFirst"><code>Color.web("#f68");</code></td>
     * <td class="colLast"><code>rgb(255, 102, 136, 1.0)</code></td>
     * </tr>
     * <tr class="altColor">
     * <td class="colFirst"><code>Color.web("rgb(255,102,136)");</code></td>
     * <td class="colLast"><code>rgb(255, 102, 136, 1.0)</code></td>
     * </tr>
     * <tr class="rowColor">
     * <td class="colFirst"><code>Color.web("rgb(100%,50%,50%)");</code></td>
     * <td class="colLast"><code>rgb(255, 128, 128, 1.0)</code></td>
     * </tr>
     * <tr class="altColor">
     * <td class="colFirst"><code>Color.web("rgb(255,50%,50%,0.25)");</code></td>
     * <td class="colLast"><code>rgb(255, 128, 128, 0.25)</code></td>
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
                return rgba(r / 15.0, g / 15.0, b / 15.0, opacity);
            } else if (len == 4) {
                r = Integer.parseInt(color.substring(0, 1), 16);
                g = Integer.parseInt(color.substring(1, 2), 16);
                b = Integer.parseInt(color.substring(2, 3), 16);
                a = Integer.parseInt(color.substring(3, 4), 16);
                return rgba(r / 15.0, g / 15.0, b / 15.0, opacity * a / 15.0);
            } else if (len == 6) {
                r = Integer.parseInt(color.substring(0, 2), 16);
                g = Integer.parseInt(color.substring(2, 4), 16);
                b = Integer.parseInt(color.substring(4, 6), 16);
                return rgba255(r, g, b, opacity);
            } else if (len == 8) {
                r = Integer.parseInt(color.substring(0, 2), 16);
                g = Integer.parseInt(color.substring(2, 4), 16);
                b = Integer.parseInt(color.substring(4, 6), 16);
                a = Integer.parseInt(color.substring(6, 8), 16);
                return rgba255(r, g, b, opacity * a / 255.0);
            }
        } catch (NumberFormatException nfe) {}

        throw new IllegalArgumentException("Invalid color specification");
    }


    public static Color hsb(double hue, double saturation, double brightness, double opacity) {
        double[] rgb = HSBtoRGB(hue, saturation, brightness);
        return rgba(rgb[0], rgb[1], rgb[2], opacity);
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
                return rgba(r, g, b, a);
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

    public static Color rgb(double red, double green, double blue) {
        return rgba(red, green, blue, 1d);
    }

    public static Color rgba(double red, double green, double blue, double opacity) {
        return new Color(red, green, blue, opacity);
    }

    public static Color rgba255(int red, int green, int blue, double opacity) {
        return rgba(red / 255.0, green / 255.0, blue / 255.0, opacity);
    }

    public static Color hsba(double hue, double saturation, double brightness, double opacity) {
        return hsb(hue, saturation, brightness, opacity);
    }

    /**
     * A fully transparent color with an ARGB value of #00000000.
     */
    public static Color TRANSPARENT = rgba(0d, 0d, 0d, 0d);

    /**
     * The color alice blue with an RGB value of #F0F8FF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F0F8FF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color ALICEBLUE = rgb(0.9411765d, 0.972549d, 1.0d);

    /**
     * The color antique white with an RGB value of #FAEBD7
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FAEBD7;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color ANTIQUEWHITE = rgb(0.98039216d, 0.92156863d, 0.84313726d);

    /**
     * The color aqua with an RGB value of #00FFFF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FFFF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color AQUA = rgb(0.0d, 1.0d, 1.0d);

    /**
     * The color aquamarine with an RGB value of #7FFFD4
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#7FFFD4;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color AQUAMARINE = rgb(0.49803922d, 1.0d, 0.83137256d);

    /**
     * The color azure with an RGB value of #F0FFFF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F0FFFF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color AZURE = rgb(0.9411765d, 1.0d, 1.0d);

    /**
     * The color beige with an RGB value of #F5F5DC
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F5F5DC;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color BEIGE = rgb(0.9607843d, 0.9607843d, 0.8627451d);

    /**
     * The color bisque with an RGB value of #FFE4C4
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFE4C4;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color BISQUE = rgb(1.0d, 0.89411765d, 0.76862746d);

    /**
     * The color black with an RGB value of #000000
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#000000;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color BLACK = rgb(0.0d, 0.0d, 0.0d);

    /**
     * The color blanched almond with an RGB value of #FFEBCD
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFEBCD;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color BLANCHEDALMOND = rgb(1.0d, 0.92156863d, 0.8039216d);

    /**
     * The color blue with an RGB value of #0000FF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#0000FF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color BLUE = rgb(0.0d, 0.0d, 1.0d);

    /**
     * The color blue violet with an RGB value of #8A2BE2
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#8A2BE2;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color BLUEVIOLET = rgb(0.5411765d, 0.16862746d, 0.8862745d);

    /**
     * The color brown with an RGB value of #A52A2A
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#A52A2A;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color BROWN = rgb(0.64705884d, 0.16470589d, 0.16470589d);

    /**
     * The color burly wood with an RGB value of #DEB887
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#DEB887;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color BURLYWOOD = rgb(0.87058824d, 0.72156864d, 0.5294118d);

    /**
     * The color cadet blue with an RGB value of #5F9EA0
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#5F9EA0;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color CADETBLUE = rgb(0.37254903d, 0.61960787d, 0.627451d);

    /**
     * The color chartreuse with an RGB value of #7FFF00
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#7FFF00;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color CHARTREUSE = rgb(0.49803922d, 1.0d, 0.0d);

    /**
     * The color chocolate with an RGB value of #D2691E
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#D2691E;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color CHOCOLATE = rgb(0.8235294d, 0.4117647d, 0.11764706d);

    /**
     * The color coral with an RGB value of #FF7F50
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF7F50;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color CORAL = rgb(1.0d, 0.49803922d, 0.3137255d);

    /**
     * The color cornflower blue with an RGB value of #6495ED
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#6495ED;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color CORNFLOWERBLUE = rgb(0.39215687d, 0.58431375d, 0.92941177d);

    /**
     * The color cornsilk with an RGB value of #FFF8DC
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFF8DC;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color CORNSILK = rgb(1.0d, 0.972549d, 0.8627451d);

    /**
     * The color crimson with an RGB value of #DC143C
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#DC143C;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color CRIMSON = rgb(0.8627451d, 0.078431375d, 0.23529412d);

    /**
     * The color cyan with an RGB value of #00FFFF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FFFF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color CYAN = rgb(0.0d, 1.0d, 1.0d);

    /**
     * The color dark blue with an RGB value of #00008B
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00008B;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKBLUE = rgb(0.0d, 0.0d, 0.54509807d);

    /**
     * The color dark cyan with an RGB value of #008B8B
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#008B8B;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKCYAN = rgb(0.0d, 0.54509807d, 0.54509807d);

    /**
     * The color dark goldenrod with an RGB value of #B8860B
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#B8860B;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKGOLDENROD = rgb(0.72156864d, 0.5254902d, 0.043137256d);

    /**
     * The color dark gray with an RGB value of #A9A9A9
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#A9A9A9;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKGRAY = rgb(0.6627451d, 0.6627451d, 0.6627451d);

    /**
     * The color dark green with an RGB value of #006400
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#006400;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKGREEN = rgb(0.0d, 0.39215687d, 0.0d);

    /**
     * The color dark grey with an RGB value of #A9A9A9
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#A9A9A9;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKGREY             = DARKGRAY;

    /**
     * The color dark khaki with an RGB value of #BDB76B
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#BDB76B;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKKHAKI = rgb(0.7411765d, 0.7176471d, 0.41960785d);

    /**
     * The color dark magenta with an RGB value of #8B008B
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#8B008B;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKMAGENTA = rgb(0.54509807d, 0.0d, 0.54509807d);

    /**
     * The color dark olive green with an RGB value of #556B2F
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#556B2F;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKOLIVEGREEN = rgb(0.33333334d, 0.41960785d, 0.18431373d);

    /**
     * The color dark orange with an RGB value of #FF8C00
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF8C00;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKORANGE = rgb(1.0d, 0.54901963d, 0.0d);

    /**
     * The color dark orchid with an RGB value of #9932CC
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#9932CC;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKORCHID = rgb(0.6d, 0.19607843d, 0.8d);

    /**
     * The color dark red with an RGB value of #8B0000
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#8B0000;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKRED = rgb(0.54509807d, 0.0d, 0.0d);

    /**
     * The color dark salmon with an RGB value of #E9967A
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#E9967A;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKSALMON = rgb(0.9137255d, 0.5882353d, 0.47843137d);

    /**
     * The color dark sea green with an RGB value of #8FBC8F
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#8FBC8F;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKSEAGREEN = rgb(0.56078434d, 0.7372549d, 0.56078434d);

    /**
     * The color dark slate blue with an RGB value of #483D8B
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#483D8B;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKSLATEBLUE = rgb(0.28235295d, 0.23921569d, 0.54509807d);

    /**
     * The color dark slate gray with an RGB value of #2F4F4F
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#2F4F4F;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKSLATEGRAY = rgb(0.18431373d, 0.30980393d, 0.30980393d);

    /**
     * The color dark slate grey with an RGB value of #2F4F4F
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#2F4F4F;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKSLATEGREY        = DARKSLATEGRAY;

    /**
     * The color dark turquoise with an RGB value of #00CED1
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00CED1;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKTURQUOISE = rgb(0.0d, 0.80784315d, 0.81960785d);

    /**
     * The color dark violet with an RGB value of #9400D3
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#9400D3;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DARKVIOLET = rgb(0.5803922d, 0.0d, 0.827451d);

    /**
     * The color deep pink with an RGB value of #FF1493
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF1493;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DEEPPINK = rgb(1.0d, 0.078431375d, 0.5764706d);

    /**
     * The color deep sky blue with an RGB value of #00BFFF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00BFFF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DEEPSKYBLUE = rgb(0.0d, 0.7490196d, 1.0d);

    /**
     * The color dim gray with an RGB value of #696969
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#696969;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DIMGRAY = rgb(0.4117647d, 0.4117647d, 0.4117647d);

    /**
     * The color dim grey with an RGB value of #696969
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#696969;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DIMGREY              = DIMGRAY;

    /**
     * The color dodger blue with an RGB value of #1E90FF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#1E90FF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color DODGERBLUE = rgb(0.11764706d, 0.5647059d, 1.0d);

    /**
     * The color firebrick with an RGB value of #B22222
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#B22222;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color FIREBRICK = rgb(0.69803923d, 0.13333334d, 0.13333334d);

    /**
     * The color floral white with an RGB value of #FFFAF0
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFAF0;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color FLORALWHITE = rgb(1.0d, 0.98039216d, 0.9411765d);

    /**
     * The color forest green with an RGB value of #228B22
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#228B22;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color FORESTGREEN = rgb(0.13333334d, 0.54509807d, 0.13333334d);

    /**
     * The color fuchsia with an RGB value of #FF00FF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF00FF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color FUCHSIA = rgb(1.0d, 0.0d, 1.0d);

    /**
     * The color gainsboro with an RGB value of #DCDCDC
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#DCDCDC;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color GAINSBORO = rgb(0.8627451d, 0.8627451d, 0.8627451d);

    /**
     * The color ghost white with an RGB value of #F8F8FF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F8F8FF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color GHOSTWHITE = rgb(0.972549d, 0.972549d, 1.0d);

    /**
     * The color gold with an RGB value of #FFD700
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFD700;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color GOLD = rgb(1.0d, 0.84313726d, 0.0d);

    /**
     * The color goldenrod with an RGB value of #DAA520
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#DAA520;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color GOLDENROD = rgb(0.85490197d, 0.64705884d, 0.1254902d);

    /**
     * The color gray with an RGB value of #808080
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#808080;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color GRAY = rgb(0.5019608d, 0.5019608d, 0.5019608d);

    /**
     * The color green with an RGB value of #008000
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#008000;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color GREEN = rgb(0.0d, 0.5019608d, 0.0d);

    /**
     * The color green yellow with an RGB value of #ADFF2F
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#ADFF2F;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color GREENYELLOW = rgb(0.6784314d, 1.0d, 0.18431373d);

    /**
     * The color grey with an RGB value of #808080
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#808080;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color GREY                 = GRAY;

    /**
     * The color honeydew with an RGB value of #F0FFF0
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F0FFF0;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color HONEYDEW = rgb(0.9411765d, 1.0d, 0.9411765d);

    /**
     * The color hot pink with an RGB value of #FF69B4
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF69B4;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color HOTPINK = rgb(1.0d, 0.4117647d, 0.7058824d);

    /**
     * The color indian red with an RGB value of #CD5C5C
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#CD5C5C;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color INDIANRED = rgb(0.8039216d, 0.36078432d, 0.36078432d);

    /**
     * The color indigo with an RGB value of #4B0082
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#4B0082;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color INDIGO = rgb(0.29411766d, 0.0d, 0.50980395d);

    /**
     * The color ivory with an RGB value of #FFFFF0
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFFF0;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color IVORY = rgb(1.0d, 1.0d, 0.9411765d);

    /**
     * The color khaki with an RGB value of #F0E68C
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F0E68C;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color KHAKI = rgb(0.9411765d, 0.9019608d, 0.54901963d);

    /**
     * The color lavender with an RGB value of #E6E6FA
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#E6E6FA;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LAVENDER = rgb(0.9019608d, 0.9019608d, 0.98039216d);

    /**
     * The color lavender blush with an RGB value of #FFF0F5
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFF0F5;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LAVENDERBLUSH = rgb(1.0d, 0.9411765d, 0.9607843d);

    /**
     * The color lawn green with an RGB value of #7CFC00
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#7CFC00;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LAWNGREEN = rgb(0.4862745d, 0.9882353d, 0.0d);

    /**
     * The color lemon chiffon with an RGB value of #FFFACD
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFACD;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LEMONCHIFFON = rgb(1.0d, 0.98039216d, 0.8039216d);

    /**
     * The color light blue with an RGB value of #ADD8E6
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#ADD8E6;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTBLUE = rgb(0.6784314d, 0.84705883d, 0.9019608d);

    /**
     * The color light coral with an RGB value of #F08080
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F08080;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTCORAL = rgb(0.9411765d, 0.5019608d, 0.5019608d);

    /**
     * The color light cyan with an RGB value of #E0FFFF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#E0FFFF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTCYAN = rgb(0.8784314d, 1.0d, 1.0d);

    /**
     * The color light goldenrod yellow with an RGB value of #FAFAD2
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FAFAD2;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTGOLDENRODYELLOW = rgb(0.98039216d, 0.98039216d, 0.8235294d);

    /**
     * The color light gray with an RGB value of #D3D3D3
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#D3D3D3;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTGRAY = rgb(0.827451d, 0.827451d, 0.827451d);

    /**
     * The color light green with an RGB value of #90EE90
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#90EE90;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTGREEN = rgb(0.5647059d, 0.93333334d, 0.5647059d);

    /**
     * The color light grey with an RGB value of #D3D3D3
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#D3D3D3;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTGREY            = LIGHTGRAY;

    /**
     * The color light pink with an RGB value of #FFB6C1
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFB6C1;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTPINK = rgb(1.0d, 0.7137255d, 0.75686276d);

    /**
     * The color light salmon with an RGB value of #FFA07A
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFA07A;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTSALMON = rgb(1.0d, 0.627451d, 0.47843137d);

    /**
     * The color light sea green with an RGB value of #20B2AA
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#20B2AA;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTSEAGREEN = rgb(0.1254902d, 0.69803923d, 0.6666667d);

    /**
     * The color light sky blue with an RGB value of #87CEFA
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#87CEFA;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTSKYBLUE = rgb(0.5294118d, 0.80784315d, 0.98039216d);

    /**
     * The color light slate gray with an RGB value of #778899
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#778899;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTSLATEGRAY = rgb(0.46666667d, 0.53333336d, 0.6d);

    /**
     * The color light slate grey with an RGB value of #778899
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#778899;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTSLATEGREY       = LIGHTSLATEGRAY;

    /**
     * The color light steel blue with an RGB value of #B0C4DE
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#B0C4DE;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTSTEELBLUE = rgb(0.6901961d, 0.76862746d, 0.87058824d);

    /**
     * The color light yellow with an RGB value of #FFFFE0
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFFE0;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIGHTYELLOW = rgb(1.0d, 1.0d, 0.8784314d);

    /**
     * The color lime with an RGB value of #00FF00
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FF00;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIME = rgb(0.0d, 1.0d, 0.0d);

    /**
     * The color lime green with an RGB value of #32CD32
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#32CD32;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LIMEGREEN = rgb(0.19607843d, 0.8039216d, 0.19607843d);

    /**
     * The color linen with an RGB value of #FAF0E6
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FAF0E6;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color LINEN = rgb(0.98039216d, 0.9411765d, 0.9019608d);

    /**
     * The color magenta with an RGB value of #FF00FF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF00FF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MAGENTA = rgb(1.0d, 0.0d, 1.0d);

    /**
     * The color maroon with an RGB value of #800000
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#800000;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MAROON = rgb(0.5019608d, 0.0d, 0.0d);

    /**
     * The color medium aquamarine with an RGB value of #66CDAA
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#66CDAA;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMAQUAMARINE = rgb(0.4d, 0.8039216d, 0.6666667d);

    /**
     * The color medium blue with an RGB value of #0000CD
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#0000CD;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMBLUE = rgb(0.0d, 0.0d, 0.8039216d);

    /**
     * The color medium orchid with an RGB value of #BA55D3
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#BA55D3;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMORCHID = rgb(0.7294118d, 0.33333334d, 0.827451d);

    /**
     * The color medium purple with an RGB value of #9370DB
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#9370DB;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMPURPLE = rgb(0.5764706d, 0.4392157d, 0.85882354d);

    /**
     * The color medium sea green with an RGB value of #3CB371
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#3CB371;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMSEAGREEN = rgb(0.23529412d, 0.7019608d, 0.44313726d);

    /**
     * The color medium slate blue with an RGB value of #7B68EE
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#7B68EE;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMSLATEBLUE = rgb(0.48235294d, 0.40784314d, 0.93333334d);

    /**
     * The color medium spring green with an RGB value of #00FA9A
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FA9A;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMSPRINGGREEN = rgb(0.0d, 0.98039216d, 0.6039216d);

    /**
     * The color medium turquoise with an RGB value of #48D1CC
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#48D1CC;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMTURQUOISE = rgb(0.28235295d, 0.81960785d, 0.8d);

    /**
     * The color medium violet red with an RGB value of #C71585
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#C71585;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MEDIUMVIOLETRED = rgb(0.78039217d, 0.08235294d, 0.52156866d);

    /**
     * The color midnight blue with an RGB value of #191970
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#191970;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MIDNIGHTBLUE = rgb(0.09803922d, 0.09803922d, 0.4392157d);

    /**
     * The color mint cream with an RGB value of #F5FFFA
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F5FFFA;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MINTCREAM = rgb(0.9607843d, 1.0d, 0.98039216d);

    /**
     * The color misty rose with an RGB value of #FFE4E1
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFE4E1;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MISTYROSE = rgb(1.0d, 0.89411765d, 0.88235295d);

    /**
     * The color moccasin with an RGB value of #FFE4B5
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFE4B5;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color MOCCASIN = rgb(1.0d, 0.89411765d, 0.70980394d);

    /**
     * The color navajo white with an RGB value of #FFDEAD
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFDEAD;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color NAVAJOWHITE = rgb(1.0d, 0.87058824d, 0.6784314d);

    /**
     * The color navy with an RGB value of #000080
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#000080;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color NAVY = rgb(0.0d, 0.0d, 0.5019608d);

    /**
     * The color old lace with an RGB value of #FDF5E6
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FDF5E6;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color OLDLACE = rgb(0.99215686d, 0.9607843d, 0.9019608d);

    /**
     * The color olive with an RGB value of #808000
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#808000;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color OLIVE = rgb(0.5019608d, 0.5019608d, 0.0d);

    /**
     * The color olive drab with an RGB value of #6B8E23
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#6B8E23;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color OLIVEDRAB = rgb(0.41960785d, 0.5568628d, 0.13725491d);

    /**
     * The color orange with an RGB value of #FFA500
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFA500;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color ORANGE = rgb(1.0d, 0.64705884d, 0.0d);

    /**
     * The color orange red with an RGB value of #FF4500
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF4500;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color ORANGERED = rgb(1.0d, 0.27058825d, 0.0d);

    /**
     * The color orchid with an RGB value of #DA70D6
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#DA70D6;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color ORCHID = rgb(0.85490197d, 0.4392157d, 0.8392157d);

    /**
     * The color pale goldenrod with an RGB value of #EEE8AA
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#EEE8AA;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PALEGOLDENROD = rgb(0.93333334d, 0.9098039d, 0.6666667d);

    /**
     * The color pale green with an RGB value of #98FB98
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#98FB98;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PALEGREEN = rgb(0.59607846d, 0.9843137d, 0.59607846d);

    /**
     * The color pale turquoise with an RGB value of #AFEEEE
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#AFEEEE;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PALETURQUOISE = rgb(0.6862745d, 0.93333334d, 0.93333334d);

    /**
     * The color pale violet red with an RGB value of #DB7093
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#DB7093;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PALEVIOLETRED = rgb(0.85882354d, 0.4392157d, 0.5764706d);

    /**
     * The color papaya whip with an RGB value of #FFEFD5
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFEFD5;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PAPAYAWHIP = rgb(1.0d, 0.9372549d, 0.8352941d);

    /**
     * The color peach puff with an RGB value of #FFDAB9
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFDAB9;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PEACHPUFF = rgb(1.0d, 0.85490197d, 0.7254902d);

    /**
     * The color peru with an RGB value of #CD853F
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#CD853F;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PERU = rgb(0.8039216d, 0.52156866d, 0.24705882d);

    /**
     * The color pink with an RGB value of #FFC0CB
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFC0CB;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PINK = rgb(1.0d, 0.7529412d, 0.79607844d);

    /**
     * The color plum with an RGB value of #DDA0DD
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#DDA0DD;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PLUM = rgb(0.8666667d, 0.627451d, 0.8666667d);

    /**
     * The color powder blue with an RGB value of #B0E0E6
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#B0E0E6;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color POWDERBLUE = rgb(0.6901961d, 0.8784314d, 0.9019608d);

    /**
     * The color purple with an RGB value of #800080
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#800080;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color PURPLE = rgb(0.5019608d, 0.0d, 0.5019608d);

    /**
     * The color red with an RGB value of #FF0000
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF0000;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color RED = rgb(1.0d, 0.0d, 0.0d);

    /**
     * The color rosy brown with an RGB value of #BC8F8F
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#BC8F8F;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color ROSYBROWN = rgb(0.7372549d, 0.56078434d, 0.56078434d);

    /**
     * The color royal blue with an RGB value of #4169E1
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#4169E1;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color ROYALBLUE = rgb(0.25490198d, 0.4117647d, 0.88235295d);

    /**
     * The color saddle brown with an RGB value of #8B4513
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#8B4513;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SADDLEBROWN = rgb(0.54509807d, 0.27058825d, 0.07450981d);

    /**
     * The color salmon with an RGB value of #FA8072
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FA8072;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SALMON = rgb(0.98039216d, 0.5019608d, 0.44705883d);

    /**
     * The color sandy brown with an RGB value of #F4A460
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F4A460;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SANDYBROWN = rgb(0.95686275d, 0.6431373d, 0.3764706d);

    /**
     * The color sea green with an RGB value of #2E8B57
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#2E8B57;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SEAGREEN = rgb(0.18039216d, 0.54509807d, 0.34117648d);

    /**
     * The color sea shell with an RGB value of #FFF5EE
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFF5EE;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SEASHELL = rgb(1.0d, 0.9607843d, 0.93333334d);

    /**
     * The color sienna with an RGB value of #A0522D
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#A0522D;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SIENNA = rgb(0.627451d, 0.32156864d, 0.1764706d);

    /**
     * The color silver with an RGB value of #C0C0C0
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#C0C0C0;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SILVER = rgb(0.7529412d, 0.7529412d, 0.7529412d);

    /**
     * The color sky blue with an RGB value of #87CEEB
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#87CEEB;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SKYBLUE = rgb(0.5294118d, 0.80784315d, 0.92156863d);

    /**
     * The color slate blue with an RGB value of #6A5ACD
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#6A5ACD;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SLATEBLUE = rgb(0.41568628d, 0.3529412d, 0.8039216d);

    /**
     * The color slate gray with an RGB value of #708090
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#708090;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SLATEGRAY = rgb(0.4392157d, 0.5019608d, 0.5647059d);

    /**
     * The color slate grey with an RGB value of #708090
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#708090;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SLATEGREY            = SLATEGRAY;

    /**
     * The color snow with an RGB value of #FFFAFA
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFAFA;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SNOW = rgb(1.0d, 0.98039216d, 0.98039216d);

    /**
     * The color spring green with an RGB value of #00FF7F
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#00FF7F;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color SPRINGGREEN = rgb(0.0d, 1.0d, 0.49803922d);

    /**
     * The color steel blue with an RGB value of #4682B4
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#4682B4;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color STEELBLUE = rgb(0.27450982d, 0.50980395d, 0.7058824d);

    /**
     * The color tan with an RGB value of #D2B48C
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#D2B48C;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color TAN = rgb(0.8235294d, 0.7058824d, 0.54901963d);

    /**
     * The color teal with an RGB value of #008080
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#008080;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color TEAL = rgb(0.0d, 0.5019608d, 0.5019608d);

    /**
     * The color thistle with an RGB value of #D8BFD8
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#D8BFD8;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color THISTLE = rgb(0.84705883d, 0.7490196d, 0.84705883d);

    /**
     * The color tomato with an RGB value of #FF6347
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FF6347;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color TOMATO = rgb(1.0d, 0.3882353d, 0.2784314d);

    /**
     * The color turquoise with an RGB value of #40E0D0
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#40E0D0;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color TURQUOISE = rgb(0.2509804d, 0.8784314d, 0.8156863d);

    /**
     * The color violet with an RGB value of #EE82EE
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#EE82EE;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color VIOLET = rgb(0.93333334d, 0.50980395d, 0.93333334d);

    /**
     * The color wheat with an RGB value of #F5DEB3
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F5DEB3;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color WHEAT = rgb(0.9607843d, 0.87058824d, 0.7019608d);

    /**
     * The color white with an RGB value of #FFFFFF
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFFFF;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color WHITE = rgb(1.0d, 1.0d, 1.0d);

    /**
     * The color white smoke with an RGB value of #F5F5F5
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#F5F5F5;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color WHITESMOKE = rgb(0.9607843d, 0.9607843d, 0.9607843d);

    /**
     * The color yellow with an RGB value of #FFFF00
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#FFFF00;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color YELLOW = rgb(1.0d, 1.0d, 0.0d);

    /**
     * The color yellow green with an RGB value of #9ACD32
     * <div style="border:1px solid black;width:40px;height:20px;background-color:#9ACD32;float:right;margin: 0 10px 0 0"></div><br/><br/>
     */
    public static Color YELLOWGREEN = rgb(0.6039216d, 0.8039216d, 0.19607843d);
}
