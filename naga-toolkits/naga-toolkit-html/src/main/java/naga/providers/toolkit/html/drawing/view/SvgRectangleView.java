package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawing;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends RectangleViewImplBase implements SvgDrawableView {

    private final SvgShapeElementUpdater svgShapeElementUpdater = new SvgShapeElementUpdater(SvgUtil.createSvgRectangle());

    @Override
    public void syncSvgPropertiesFromDrawable(SvgDrawing svgDrawingNode) {
        Rectangle r = drawable;
        svgShapeElementUpdater.syncSvgFromCommonShapeProperties(r, svgDrawingNode);
        svgShapeElementUpdater.setSvgAttribute("x", r.getX());
        svgShapeElementUpdater.setSvgAttribute("y", r.getY());
        svgShapeElementUpdater.setSvgAttribute("width", r.getWidth());
        svgShapeElementUpdater.setSvgAttribute("height", r.getHeight());
        svgShapeElementUpdater.setSvgAttribute("rx", r.getArcWidth());
        svgShapeElementUpdater.setSvgAttribute("ry", r.getArcHeight());
    }

    @Override
    public Element getSvgDrawableElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }
}
