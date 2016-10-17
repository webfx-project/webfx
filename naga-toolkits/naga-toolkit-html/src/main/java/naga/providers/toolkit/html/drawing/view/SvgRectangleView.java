package naga.providers.toolkit.html.drawing.view;

import elemental2.Element;
import javafx.beans.value.ChangeListener;
import naga.providers.toolkit.html.drawing.SvgUtil;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public class SvgRectangleView implements RectangleView, SvgShapeView {

    private Rectangle rectangle;
    private Element svgRectangle;

    @Override
    public void bind(Rectangle rectangle) {
        this.rectangle = rectangle;
        svgRectangle = SvgUtil.createSvgRectangle();
        syncVisual();
        ChangeListener<Double> changeListener = (observable, oldValue, newValue) -> syncVisual();
        rectangle.xProperty().addListener(changeListener);
        rectangle.yProperty().addListener(changeListener);
        rectangle.widthProperty().addListener(changeListener);
        rectangle.heightProperty().addListener(changeListener);
    }

    private void syncVisual() {
        svgRectangle.setAttribute("x", rectangle.getX());
        svgRectangle.setAttribute("y", rectangle.getY());
        svgRectangle.setAttribute("width", rectangle.getWidth());
        svgRectangle.setAttribute("height", rectangle.getHeight());
        svgRectangle.setAttribute("fill", "red");
    }

    @Override
    public void unbind() {
        svgRectangle = null;
    }

    @Override
    public Element getSvgShapeElement() {
        return svgRectangle;
    }
}
