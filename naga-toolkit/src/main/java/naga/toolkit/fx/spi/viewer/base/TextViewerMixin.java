package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.spi.viewer.TextViewer;

/**
 * @author Bruno Salmon
 */
public interface TextViewerMixin
        extends TextViewer,
        ShapeViewerMixin<Text, TextViewerBase, TextViewerMixin> {

    void updateText(String text);

    void updateTextOrigin(VPos textOrigin);

    void updateX(Double x);

    void updateY(Double y);

    void updateWrappingWidth(Double wrappingWidth);

    void updateTextAlignment(TextAlignment textAlignment);

    void updateFont(Font font);

}
