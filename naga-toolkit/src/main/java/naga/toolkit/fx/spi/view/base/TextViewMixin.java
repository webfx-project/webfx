package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.spi.view.TextView;

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
