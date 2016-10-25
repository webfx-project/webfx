package naga.providers.toolkit.html.drawing;

import elemental2.Element;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.providers.toolkit.html.util.HtmlUtil;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.CycleMethod;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.paint.Stop;
import naga.toolkit.drawing.shapes.StrokeLineCap;
import naga.toolkit.drawing.shapes.StrokeLineJoin;

import static elemental2.Global.document;

/**
 * @author Bruno Salmon
 */
public class SvgUtil {

    private final static String svgNS = "http://www.w3.org/2000/svg";

    public static Element createSvgElement(String tag) {
        return document.createElementNS(svgNS, tag);
    }

    public static /*SVGElement Elemental2 compilation error */ Element createSvgElement() {
        return /*SVGElement*/ createSvgElement("svg");
    }

    public static Element createSvgDefs() {
        return createSvgElement("defs");
    }

    public static Element createSvgRectangle() {
        return createSvgElement("rect");
    }

    private static int lgSeq;
    public static Element createLinearGradient() {
        return HtmlUtil.setAttribute(createSvgElement("linearGradient"), "id", "LG" + ++lgSeq);
    }

    public static Element updateLinearGradient(LinearGradient lg, Element svgLg) {
        if (svgLg == null)
            svgLg = createLinearGradient();
        svgLg.setAttribute("x1", lg.getStartX());
        svgLg.setAttribute("y1", lg.getStartY());
        svgLg.setAttribute("x2", lg.getEndX());
        svgLg.setAttribute("y2", lg.getEndY());
        CycleMethod cycleMethod = lg.getCycleMethod();
        svgLg.setAttribute("spreadMethod", cycleMethod == CycleMethod.REPEAT ? "repeat" : cycleMethod == CycleMethod.REFLECT ? "reflect" : "pad");
        svgLg.setAttribute("gradientUnits", lg.isProportional() ? "objectBoundingBox" : "userSpaceOnUse");
        return HtmlUtil.setChildren(svgLg, Collections.convert(lg.getStops(), SvgUtil::toSvgStop));
    }

    private static Element toSvgStop(Stop stop) {
        Element svgStop = createSvgElement("stop");
        svgStop.setAttribute("offset", (stop.getOffset() * 100) + "%");
        Color stopColor = stop.getColor();
        svgStop.setAttribute("stop-color", HtmlPaints.toCssOpaqueColor(stopColor));
        if (!stopColor.isOpaque())
            svgStop.setAttribute("stop-opacity", stopColor.getOpacity());
        return svgStop;
    }

    public static String toSvgStrokeLineCap(StrokeLineCap strokeLineCap) {
        if (strokeLineCap != null)
            switch (strokeLineCap) {
                case BUTT: return "butt";
                case ROUND: return "round";
                case SQUARE: return "square";
            }
        return null;
    }

    public static String toSvgStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        if (strokeLineJoin != null)
            switch (strokeLineJoin) {
                case BEVEL: return "bevel";
                case MITER: return "miter";
                case ROUND: return "round";
            }
        return null;
    }
}
