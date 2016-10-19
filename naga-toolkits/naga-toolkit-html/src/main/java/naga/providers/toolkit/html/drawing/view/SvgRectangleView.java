package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.RectangleView;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView extends RectangleViewImplBase implements RectangleView, SvgShapeView {

    private final Element svgRectangle = SvgUtil.createSvgRectangle();

    @Override
    public void bind(Rectangle rectangle, DrawingNotifier drawingNotifier) {
        super.bind(rectangle, drawingNotifier);
        runNowAndOnPropertyChange(this::syncVisual, rectangle.xProperty(), rectangle.yProperty(), rectangle.widthProperty(), rectangle.heightProperty());
    }

    private void syncVisual() {
        svgRectangle.setAttribute("x", shape.getX());
        svgRectangle.setAttribute("y", shape.getY());
        svgRectangle.setAttribute("width", shape.getWidth());
        svgRectangle.setAttribute("height", shape.getHeight());
        svgRectangle.setAttribute("fill", "red");
    }

    @Override
    public Element getSvgShapeElement() {
        return svgRectangle;
    }
}
