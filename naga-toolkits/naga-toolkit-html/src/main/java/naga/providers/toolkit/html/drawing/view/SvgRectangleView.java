package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends RectangleViewImplBase implements SvgShapeView {

    private final SvgShapeElementUpdater svgShapeElementUpdater = new SvgShapeElementUpdater(SvgUtil.createSvgRectangle());

    @Override
    public void syncSvgPropertiesFromShape(SvgDrawingNode svgDrawingNode) {
        Element svgElement = svgShapeElementUpdater.syncSvgFromCommonShapeProperties(shape, svgDrawingNode);
        svgElement.setAttribute("x", shape.getX());
        svgElement.setAttribute("y", shape.getY());
        svgElement.setAttribute("width", shape.getWidth());
        svgElement.setAttribute("height", shape.getHeight());
        svgElement.setAttribute("rx", shape.getArcWidth());
        svgElement.setAttribute("ry", shape.getArcHeight());
    }

    @Override
    public Element getSvgShapeElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }
}
