package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.RectangleView;

/**
 * @author Bruno Salmon
 */
public class RectangleViewBase extends ShapeViewBase<Rectangle> implements RectangleView {

    @Override
    public void bind(Rectangle r, DrawingRequester drawingRequester) {
        super.bind(r, drawingRequester);
        requestDrawableViewUpdateOnPropertiesChange(drawingRequester,
                r.xProperty(),
                r.yProperty(),
                r.widthProperty(),
                r.heightProperty(),
                r.arcWidthProperty(),
                r.arcHeightProperty());
    }
}
