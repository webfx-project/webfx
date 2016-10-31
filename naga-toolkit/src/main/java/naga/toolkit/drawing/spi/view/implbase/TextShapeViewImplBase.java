package naga.toolkit.drawing.spi.view.implbase;

import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.TextShapeView;

/**
 * @author Bruno Salmon
 */
public class TextShapeViewImplBase extends ShapeViewImplBase<TextShape> implements TextShapeView {

    @Override
    public void bind(TextShape shape, DrawingNotifier drawingNotifier) {
        super.bind(shape, drawingNotifier);
        requestRepaintShapeOnPropertiesChange(drawingNotifier, shape,
                shape.textProperty(),
                shape.textOriginProperty(),
                shape.wrappingWidthProperty(),
                shape.textAlignmentProperty(),
                shape.fontProperty());
    }

}
