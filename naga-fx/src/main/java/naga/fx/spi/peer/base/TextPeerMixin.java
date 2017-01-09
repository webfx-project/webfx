package naga.fx.spi.peer.base;

import naga.fx.geometry.VPos;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.Text;
import naga.fx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public interface TextPeerMixin
        <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>

        extends ShapePeerMixin<N, NB, NM> {

    void updateText(String text);

    void updateTextOrigin(VPos textOrigin);

    void updateX(Double x);

    void updateY(Double y);

    void updateWrappingWidth(Double wrappingWidth);

    void updateTextAlignment(TextAlignment textAlignment);

    void updateFont(Font font);

}
