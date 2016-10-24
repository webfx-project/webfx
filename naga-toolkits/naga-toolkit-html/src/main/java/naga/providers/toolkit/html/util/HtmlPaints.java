package naga.providers.toolkit.html.util;

import naga.toolkit.drawing.paint.*;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public class HtmlPaints {

    public static String toCssPaint(Paint paint) {
        if (paint instanceof Color)
            return toCssColor((Color) paint);
        if (paint instanceof LinearGradient)
            return toCssLinearGradient((LinearGradient) paint);
        return null;
    }

    public static String toCssColor(Color color) {
        return toCssColor(color, new StringBuilder()).toString();
    }

    public static String toCssOpaqueColor(Color color) {
        return toCssOpaqueColor(color, new StringBuilder()).toString();
    }

    private static StringBuilder toCssColor(Color color, StringBuilder sb) {
        return color.isOpaque() ? toCssOpaqueColor(color, sb) : toCssTransparentColor(color, sb);
    }

    private static StringBuilder toCssOpaqueColor(Color color, StringBuilder sb) {
        sb.append('#');
        appendHex2digits(to8bits(color.getRed()), sb);
        appendHex2digits(to8bits(color.getGreen()), sb);
        appendHex2digits(to8bits(color.getBlue()), sb);
        return sb;
    }

    private static StringBuilder toCssTransparentColor(Color color, StringBuilder sb) {
        return sb.append("rgba(")
                .append(to8bits(color.getRed())).append(',')
                .append(to8bits(color.getGreen())).append(',')
                .append(to8bits(color.getBlue())).append(',')
                .append(color.getOpacity()).append(')');
    }

    public static String toCssLinearGradient(LinearGradient lg) {
        CycleMethod m = lg.getCycleMethod();
        StringBuilder sb = new StringBuilder(m == CycleMethod.REPEAT ? "repeating-" : "");
        sb.append("linear-gradient(");
        sb.append(lg.getAngleDegree()).append("deg");
        toCssStops(lg.getStops(), lg.getLength(), lg.isProportional(), sb);
        sb.append(')');
        return sb.toString();
    }

    private static void toCssStops(List<Stop> stops, double length, boolean proportional, StringBuilder sb) {
        for (Stop stop : stops)
            toCssStop(stop, length, proportional, sb.append(", "));
    }

    private static void toCssStop(Stop stop, double length, boolean proportional, StringBuilder sb) {
        toCssColor(stop.getColor(), sb).append(' ');
        if (proportional)
            sb.append(stop.getOffset() * 100).append("%");
        else
            sb.append(stop.getOffset() * length).append("px");
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
