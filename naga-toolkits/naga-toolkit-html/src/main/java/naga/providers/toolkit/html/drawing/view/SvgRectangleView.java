package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.toolkit.drawing.paint.Color;
import naga.toolkit.drawing.paint.LinearGradient;
import naga.toolkit.drawing.paint.Paint;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends RectangleViewImplBase implements SvgShapeView {

    private final Element svgRectangle = SvgUtil.createSvgRectangle();
    private Element svgLinearGradient;

    @Override
    public void syncSvgPropertiesFromShape(SvgDrawingNode svgDrawingNode) {
        svgRectangle.setAttribute("x", shape.getX());
        svgRectangle.setAttribute("y", shape.getY());
        svgRectangle.setAttribute("width", shape.getWidth());
        svgRectangle.setAttribute("height", shape.getHeight());
        Paint fill = shape.getFill();
        String fillValue = null;
        if (fill instanceof Color)
            fillValue = HtmlPaints.toCssPaint(fill);
        if (fill instanceof LinearGradient) {
            if (svgLinearGradient != (svgLinearGradient = SvgUtil.updateLinearGradient((LinearGradient) fill, svgLinearGradient)))
                svgDrawingNode.addDef(svgLinearGradient);
            fillValue = "url(#" + svgLinearGradient.getAttribute("id") + ")";
        }
        svgRectangle.setAttribute("fill", fillValue);
    }

    @Override
    public Element getSvgShapeElement() {
        return svgRectangle;
    }
}
