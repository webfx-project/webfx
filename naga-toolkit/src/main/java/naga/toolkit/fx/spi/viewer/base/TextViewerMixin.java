package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public interface TextViewerMixin
        <N extends Text, NB extends TextViewerBase<N, NB, NM>, NM extends TextViewerMixin<N, NB, NM>>

        extends ShapeViewerMixin<N, NB, NM> {

    void updateText(String text);

    void updateTextOrigin(VPos textOrigin);

    void updateX(Double x);

    void updateY(Double y);

    void updateWrappingWidth(Double wrappingWidth);

    void updateTextAlignment(TextAlignment textAlignment);

    void updateFont(Font font);

}
