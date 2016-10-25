package naga.toolkit.drawing.spi.view.implbase;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public class RectangleViewImplBase extends ShapeViewImplBase<Rectangle> implements RectangleView {

    @Override
    public void bind(Rectangle shape, DrawingNotifier drawingNotifier) {
        super.bind(shape, drawingNotifier);
        requestRepaintShapeOnPropertiesChange(drawingNotifier, shape, shape.xProperty(), shape.yProperty(), shape.widthProperty(), shape.heightProperty(), shape.fillProperty(), shape.strokeProperty());
    }
}
