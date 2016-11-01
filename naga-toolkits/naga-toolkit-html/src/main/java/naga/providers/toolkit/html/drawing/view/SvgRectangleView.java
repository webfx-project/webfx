package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgDrawingNode;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends RectangleViewImplBase implements SvgDrawableView {

    private final SvgShapeElementUpdater svgShapeElementUpdater = new SvgShapeElementUpdater(SvgUtil.createSvgRectangle());

    @Override
    public void syncSvgPropertiesFromDrawable(SvgDrawingNode svgDrawingNode) {
        svgShapeElementUpdater.syncSvgFromCommonShapeProperties(drawable, svgDrawingNode);
        svgShapeElementUpdater.setSvgAttribute("x", drawable.getX());
        svgShapeElementUpdater.setSvgAttribute("y", drawable.getY());
        svgShapeElementUpdater.setSvgAttribute("width", drawable.getWidth());
        svgShapeElementUpdater.setSvgAttribute("height", drawable.getHeight());
        svgShapeElementUpdater.setSvgAttribute("rx", drawable.getArcWidth());
        svgShapeElementUpdater.setSvgAttribute("ry", drawable.getArcHeight());
    }

    @Override
    public Element getSvgDrawableElement() {
        return svgShapeElementUpdater.getSvgShapeElement();
    }
}
