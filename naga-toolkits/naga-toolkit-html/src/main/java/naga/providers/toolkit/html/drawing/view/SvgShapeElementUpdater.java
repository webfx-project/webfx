package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.VPos;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author Bruno Salmon
 */
class SvgShapeElementUpdater {

    private final Element svgElement;
    private Map<String, Element> svgLinearGradients;

    SvgShapeElementUpdater(Element svgElement) {
        this.svgElement = svgElement;
    }

    Element getSvgShapeElement() {
        return svgElement;
    }

    Element syncSvgFromCommonShapeProperties(Shape shape, SvgDrawingNode svgDrawingNode) {
        setPaintAttribute("fill", shape.getFill(), svgDrawingNode);
        setPaintAttribute("stroke", shape.getStroke(), svgDrawingNode);
        setSvgAttribute("shape-rendering", shape.isSmooth() ? "geometricPrecision" : "crispEdges");
        setSvgAttribute("stroke-width", shape.getStrokeWidth());
        setSvgAttribute("stroke-linecap", SvgUtil.toSvgStrokeLineCap(shape.getStrokeLineCap()));
        setSvgAttribute("stroke-linejoin", SvgUtil.toSvgStrokeLineJoin(shape.getStrokeLineJoin()));
        setSvgAttribute("stroke-miterlimit", shape.getStrokeMiterLimit());
        setSvgAttribute("stroke-dasharray", Collections.toStringWithNoBrackets(shape.getStrokeDashArray()));
        setSvgAttribute("stroke-dashoffset", shape.getStrokeDashOffset());
        return svgElement;
    }

    void setSvgAttribute(String name, String value) {
        setSvgAttribute(name, value, null);
    }

    void setSvgAttribute(String name, String value, String skipValue) {
        if (Objects.equals(value, skipValue))
            svgElement.removeAttribute(name);
        else
            svgElement.setAttribute(name, value);
    }

    void setSvgAttribute(String name, Double value) {
        setSvgAttribute(name, value, null);
    }

    void setSvgAttribute(String name, Double value, Double skipValue) {
        if (Objects.equals(value, skipValue))
            svgElement.removeAttribute(name);
        else
            svgElement.setAttribute(name, value);
    }

    void setSvgAttribute(String name, Integer value) {
        setSvgAttribute(name, value, null);
    }

    void setSvgAttribute(String name, Integer value, Integer skipValue) {
        if (Objects.equals(value, skipValue))
            svgElement.removeAttribute(name);
        else
            svgElement.setAttribute(name, value);
    }

    private void setPaintAttribute(String name, Paint paint, SvgDrawingNode svgDrawingNode) {
        String value = null;
        if (paint instanceof Color)
            value = HtmlPaints.toCssPaint(paint);
        else if (paint instanceof LinearGradient) {
            if (svgLinearGradients == null)
                svgLinearGradients = new HashMap<>();
            Element svgLinearGradient = svgLinearGradients.get(name);
            if (svgLinearGradient == null)
                svgLinearGradients.put(name, svgLinearGradient = svgDrawingNode.addDef(SvgUtil.createLinearGradient()));
            SvgUtil.updateLinearGradient((LinearGradient) paint, svgLinearGradient);
            value = "url(#" + svgLinearGradient.getAttribute("id") + ")";
        }
        setSvgAttribute(name, value);
    }

    static String vPosToSvgAlignmentBaseLine(VPos vpos) {
        if (vpos != null)
            switch (vpos) {
                case TOP: return "text-before-edge";
                case CENTER: return "central";
                case BASELINE: return "baseline";
                case BOTTOM: return "text-after-edge";
            }
        return null;
    }

    static String textAlignmentToSvgTextAnchor(TextAlignment textAlignment) {
        if (textAlignment != null)
            switch (textAlignment) {
                case LEFT: return "start";
                case CENTER: return "middle";
                case RIGHT: return "end";
            }
        return null;
    }

}