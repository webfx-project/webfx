package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.DrawingRequester;
import naga.toolkit.drawing.spi.view.TextShapeView;

/**
 * @author Bruno Salmon
 */
public class TextShapeViewBase extends ShapeViewBase<TextShape> implements TextShapeView {

    @Override
    public void bind(TextShape ts, DrawingRequester drawingRequester) {
        super.bind(ts, drawingRequester);
        requestDrawableViewUpdateOnPropertiesChange(drawingRequester,
                ts.textProperty(),
                ts.textOriginProperty(),
                ts.wrappingWidthProperty(),
                ts.textAlignmentProperty(),
                ts.fontProperty());
    }

}
