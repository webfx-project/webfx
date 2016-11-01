package naga.toolkit.drawing.spi.view.implbase;

import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.TextShapeView;

/**
 * @author Bruno Salmon
 */
public class TextShapeViewImplBase extends ShapeViewImplBase<TextShape> implements TextShapeView {

    @Override
    public void bind(TextShape ts, DrawingNotifier drawingNotifier) {
        super.bind(ts, drawingNotifier);
        requestRepaintDrawableOnPropertiesChange(drawingNotifier, ts,
                ts.textProperty(),
                ts.textOriginProperty(),
                ts.wrappingWidthProperty(),
                ts.textAlignmentProperty(),
                ts.fontProperty());
    }

}
