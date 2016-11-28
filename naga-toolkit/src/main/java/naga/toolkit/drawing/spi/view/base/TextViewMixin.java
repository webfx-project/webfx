package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.text.Font;
import naga.toolkit.drawing.text.Text;
import naga.toolkit.drawing.text.TextAlignment;
import naga.toolkit.drawing.geometry.VPos;
import naga.toolkit.drawing.spi.view.TextView;

/**
 * @author Bruno Salmon
 */
public interface TextViewMixin
        extends TextView,
                ShapeViewMixin<Text, TextViewBase, TextViewMixin> {

    void updateText(String text);

    void updateTextOrigin(VPos textOrigin);

    void updateX(Double x);

    void updateY(Double y);

    void updateWrappingWidth(Double wrappingWidth);

    void updateTextAlignment(TextAlignment textAlignment);

    void updateFont(Font font);

}
