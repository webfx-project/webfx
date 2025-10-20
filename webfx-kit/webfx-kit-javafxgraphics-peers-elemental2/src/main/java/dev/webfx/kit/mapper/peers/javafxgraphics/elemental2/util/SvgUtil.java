package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util;

import dev.webfx.platform.util.collection.Collections;
import elemental2.dom.Element;
import elemental2.svg.SVGSVGElement;
import javafx.scene.paint.*;
import javafx.scene.shape.StrokeLineCap;
import javafx.scene.shape.StrokeLineJoin;
import javafx.scene.shape.StrokeType;

import static elemental2.dom.DomGlobal.document;

/**
 * @author Bruno Salmon
 */
public final class SvgUtil {

    private final static String svgNS = "http://www.w3.org/2000/svg";

    public static Element createSvgElement(String tag) {
        return document.createElementNS(svgNS, tag);
    }

    public static SVGSVGElement createSvgElement() {
        return (SVGSVGElement) createSvgElement("svg");
    }

    public static Element createSvgDefs() {
        return createSvgElement("defs");
    }

    public static Element createSvgLine() {
        return createSvgElement("line");
    }

    public static Element createSvgRectangle() {
        return createSvgElement("rect");
    }

    public static Element createSvgCircle() {
        return createSvgElement("circle");
    }

    public static Element createSvgPath() {
        return createSvgElement("path");
    }

    public static Element createSvgText() {
        return createSvgElement("text");
    }

    public static Element createSvgGroup() {
        return createSvgElement("g");
    }

    public static String getDefUrl(Element defElement) {
        return defElement == null ? null : "url(#" + defElement.getAttribute("id") + ")";
    }

    private static int clipSeq;
    public static Element createClipPath() {
        return HtmlUtil.setAttribute(createSvgElement("clipPath"), "id", "CLIP" + ++clipSeq);
    }

    private static int lgSeq;
    public static Element createLinearGradient() {
        return HtmlUtil.setAttribute(createSvgElement("linearGradient"), "id", "LG" + ++lgSeq);
    }

    private static int rgSeq;
    public static Element createRadialGradient() {
        return HtmlUtil.setAttribute(createSvgElement("radialGradient"), "id", "RG" + ++rgSeq);
    }

    public static Element createFilter() {
        return HtmlUtil.setAttribute(createSvgElement("filter"), "id", generateNewFilterId());
    }

    private static int filterSeq;
    public static String generateNewFilterId() {
        return "F" + ++filterSeq;
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
        return HtmlUtil.setChildren(svgLg, Collections.map(lg.getStops(), SvgUtil::toSvgStop));
    }

    public static Element updateRadialGradient(RadialGradient rg, Element svgRg) {
        if (svgRg == null)
            svgRg = createRadialGradient();
        svgRg.setAttribute("cx", rg.getCenterX());
        svgRg.setAttribute("cy", rg.getCenterY());
        svgRg.setAttribute("r", rg.getRadius());
        CycleMethod cycleMethod = rg.getCycleMethod();
        svgRg.setAttribute("spreadMethod", cycleMethod == CycleMethod.REPEAT ? "repeat" : cycleMethod == CycleMethod.REFLECT ? "reflect" : "pad");
        svgRg.setAttribute("gradientUnits", rg.isProportional() ? "objectBoundingBox" : "userSpaceOnUse");
        return HtmlUtil.setChildren(svgRg, Collections.map(rg.getStops(), SvgUtil::toSvgStop));
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
        if (strokeLineCap != null) {
            switch (strokeLineCap) {
                case BUTT: return "butt";
                case ROUND: return "round";
                case SQUARE: return "square";
            }
        }
        return null;
    }

    public static String toSvgStrokeLineJoin(StrokeLineJoin strokeLineJoin) {
        if (strokeLineJoin != null) {
            switch (strokeLineJoin) {
                case BEVEL: return "bevel";
                case MITER: return "miter";
                case ROUND: return "round";
            }
        }
        return null;
    }

    public static String toSvgStrokeAlignment(StrokeType strokeType) {
        if (strokeType != null) {
            switch (strokeType) {
                case CENTERED: return "center";
                case INSIDE: return "inner";
                case OUTSIDE: return "outer";
            }
        }
        return null;
    }
}
