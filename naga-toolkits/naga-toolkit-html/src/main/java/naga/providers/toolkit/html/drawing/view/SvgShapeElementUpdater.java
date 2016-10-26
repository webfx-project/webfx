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

import java.util.HashMap;
import java.util.Map;

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

    Element syncSvgPropertiesFromShape(Shape shape, SvgDrawingNode svgDrawingNode) {
        setPaintAttribute("fill", shape.getFill(), svgDrawingNode);
        setPaintAttribute("stroke", shape.getStroke(), svgDrawingNode);
        svgElement.setAttribute("stroke-width", shape.getStrokeWidth());
        svgElement.setAttribute("stroke-linecap", SvgUtil.toSvgStrokeLineCap(shape.getStrokeLineCap()));
        svgElement.setAttribute("stroke-linejoin", SvgUtil.toSvgStrokeLineJoin(shape.getStrokeLineJoin()));
        svgElement.setAttribute("stroke-miterlimit", shape.getStrokeMiterLimit());
        svgElement.setAttribute("stroke-dasharray", Collections.toStringWithNoBrackets(shape.getStrokeDashArray()));
        svgElement.setAttribute("stroke-dashoffset", shape.getStrokeDashOffset());
        return svgElement;
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
        svgElement.setAttribute(name, value);
    }

}