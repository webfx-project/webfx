package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util;

import javafx.scene.paint.*;

import java.util.List;

/**
 * @author Bruno Salmon
 */
public final class HtmlPaints {

    public static String toHtmlCssPaint(Paint paint) {
        return toCssPaint(paint, DomType.HTML);
    }

    public static String toSvgCssPaint(Paint paint) {
        return toCssPaint(paint, DomType.SVG);
    }

    public static String toCssPaint(Paint paint, DomType domType) {
        if (paint instanceof Color)
            return toCssColor((Color) paint);
        if (paint instanceof LinearGradient)
            return toCssLinearGradient((LinearGradient) paint, domType);
        if (paint instanceof RadialGradient)
            return toCssRadialGradient((RadialGradient) paint, domType);
        return null;
    }

    public static String toCssColor(Color color) {
        return color == null ? null : toCssColor(color, new StringBuilder()).toString();
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

    public static String toCssLinearGradient(LinearGradient lg, DomType domType) {
        CycleMethod m = lg.getCycleMethod();
        StringBuilder sb = new StringBuilder(m == CycleMethod.REPEAT ? "repeating-" : "");
        sb.append("linear-gradient(");
        double angleDegree = lg.getAngleDegree();
        if (domType == DomType.HTML)
            angleDegree = angleDegree + 90;
        sb.append(angleDegree).append("deg");
        toCssStops(lg.getStops(), lg.getLength(), lg.isProportional(), sb);
        sb.append(')');
        return sb.toString();
    }

    public static String toCssRadialGradient(RadialGradient rg, DomType domType) {
        CycleMethod m = rg.getCycleMethod();
        StringBuilder sb = new StringBuilder(m == CycleMethod.REPEAT ? "repeating-" : "");
        sb.append("radial-gradient(circle at ");
        appendOffset(rg.getCenterX() + rg.getRadius(), rg.getRadius(), rg.isProportional(), sb);
        appendOffset(rg.getCenterY() + rg.getRadius(), rg.getRadius(), rg.isProportional(), sb.append(' '));
        toCssStops(rg.getStops(), rg.getRadius(), rg.isProportional(), sb);
        sb.append(')');
        return sb.toString();
    }

    private static void toCssStops(List<Stop> stops, double length, boolean proportional, StringBuilder sb) {
        for (Stop stop : stops)
            toCssStop(stop, length, proportional, sb.append(", "));
    }

    private static void toCssStop(Stop stop, double length, boolean proportional, StringBuilder sb) {
        toCssColor(stop.getColor(), sb).append(' ');
        appendOffset(stop.getOffset(), length, proportional, sb);
    }

    private static void appendOffset(double offset, double length, boolean proportional, StringBuilder sb) {
        if (proportional)
            sb.append(offset * 100).append("%");
        else
            sb.append(offset * length).append("px");
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
