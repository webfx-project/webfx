package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

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

    void updateLineSpacing(Number lineSpacing);

    void updateStrikethrough(Boolean strikethrough);

    void updateTextAlignment(TextAlignment textAlignment);

    void updateFont(Font font);

    void updateLineClamp(int lineClamp);

}
