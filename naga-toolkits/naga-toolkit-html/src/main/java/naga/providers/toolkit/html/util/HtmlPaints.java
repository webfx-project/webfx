package naga.providers.toolkit.html.util;

/**
 * @author Bruno Salmon
 */
public class HtmlPaints {

    public static String toHexPaint(naga.toolkit.drawing.paint.Paint paint) {
        return toHexColor((naga.toolkit.drawing.paint.Color) paint);
    }

    public static String toHexColor(naga.toolkit.drawing.paint.Color color) {
        int red = to8bits(color.getRed());
        int green = to8bits(color.getGreen());
        int blue = to8bits(color.getBlue());
        double opacity = color.getOpacity();
        if (opacity < 0.999)
            return "rgba(" + red + "," + green + "," + blue + "," + opacity + ")";
        StringBuilder sb = new StringBuilder("#");
        appendHex2digits(red, sb);
        appendHex2digits(green, sb);
        appendHex2digits(blue, sb);
        return sb.toString();
    }

    private static int to8bits(double value) {
        return (int) (value * 255 + 0.5);
    }

    private static StringBuilder appendHex2digits(int value, StringBuilder sb) {
        if (value < 16)
            sb.append('0');
        return sb.append(Integer.toHexString(value));
    }

}
