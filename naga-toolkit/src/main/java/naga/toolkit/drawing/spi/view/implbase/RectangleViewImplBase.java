package naga.toolkit.drawing.spi.view.implbase;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public class RectangleViewImplBase extends ShapeViewImplBase<Rectangle> implements RectangleView {

    @Override
    public void bind(Rectangle r, DrawingNotifier drawingNotifier) {
        super.bind(r, drawingNotifier);
        requestRepaintDrawableOnPropertiesChange(drawingNotifier, r,
                r.xProperty(),
                r.yProperty(),
                r.widthProperty(),
                r.heightProperty(),
                r.arcWidthProperty(),
                r.arcHeightProperty());
    }
}
