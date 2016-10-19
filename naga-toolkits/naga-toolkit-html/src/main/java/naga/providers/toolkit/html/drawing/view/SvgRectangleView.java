package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.providers.toolkit.html.util.HtmlPaints;
import naga.toolkit.drawing.spi.view.RectangleView;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends RectangleViewImplBase implements RectangleView, SvgShapeView {

    private final Element svgRectangle = SvgUtil.createSvgRectangle();

    @Override
    public void syncSvgPropertiesFromShape() {
        svgRectangle.setAttribute("x", shape.getX());
        svgRectangle.setAttribute("y", shape.getY());
        svgRectangle.setAttribute("width", shape.getWidth());
        svgRectangle.setAttribute("height", shape.getHeight());
        svgRectangle.setAttribute("fill", HtmlPaints.toHexPaint(shape.getFill()));
    }

    @Override
    public Element getSvgShapeElement() {
        return svgRectangle;
    }
}
