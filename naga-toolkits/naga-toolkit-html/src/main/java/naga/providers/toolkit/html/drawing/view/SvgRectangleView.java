package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.commons.util.collection.Collections;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

import java.util.HashMap;
import java.util.Map;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends RectangleViewImplBase implements SvgShapeView {

    private final Element svgRectangle = SvgUtil.createSvgRectangle();
    private Map<String, Element> svgLinearGradients;

    @Override
    public void syncSvgPropertiesFromShape(SvgDrawingNode svgDrawingNode) {
        svgRectangle.setAttribute("x", shape.getX());
        svgRectangle.setAttribute("y", shape.getY());
        svgRectangle.setAttribute("width", shape.getWidth());
        svgRectangle.setAttribute("height", shape.getHeight());
        svgRectangle.setAttribute("rx", shape.getArcWidth());
        svgRectangle.setAttribute("ry", shape.getArcHeight());
        setPaintAttribute("fill", shape.getFill(), svgDrawingNode);
        setPaintAttribute("stroke", shape.getStroke(), svgDrawingNode);
        svgRectangle.setAttribute("stroke-width", shape.getStrokeWidth());
        svgRectangle.setAttribute("stroke-linecap", SvgUtil.toSvgStrokeLineCap(shape.getStrokeLineCap()));
        svgRectangle.setAttribute("stroke-linejoin", SvgUtil.toSvgStrokeLineJoin(shape.getStrokeLineJoin()));
        svgRectangle.setAttribute("stroke-miterlimit", shape.getStrokeMiterLimit());
        svgRectangle.setAttribute("stroke-dasharray", Collections.toStringWithNoBrackets(shape.getStrokeDashArray()));
        svgRectangle.setAttribute("stroke-dashoffset", shape.getStrokeDashOffset());
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
        svgRectangle.setAttribute(name, value);
    }

    @Override
    public Element getSvgShapeElement() {
        return svgRectangle;
    }
}
