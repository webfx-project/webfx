package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.Font;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.shapes.VPos;
import naga.toolkit.drawing.spi.view.TextShapeView;

/**
 * @author Bruno Salmon
 */
public interface TextShapeViewMixin
        extends TextShapeView,
                ShapeViewMixin<TextShape, TextShapeViewBase, TextShapeViewMixin> {

    void updateText(String text);

    void updateTextOrigin(VPos textOrigin);

    void updateX(Double x);

    void updateY(Double y);

    void updateWrappingWidth(Double wrappingWidth);

    void updateTextAlignment(TextAlignment textAlignment);

    void updateFont(Font font);

}
