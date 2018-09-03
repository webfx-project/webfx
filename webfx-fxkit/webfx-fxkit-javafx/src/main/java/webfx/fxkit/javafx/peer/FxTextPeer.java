package webfx.fxkit.javafx.peer;

import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;
import webfx.fxkits.core.spi.peer.base.TextPeerBase;
import webfx.fxkits.core.spi.peer.base.TextPeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxTextPeer
        <FxN extends javafx.scene.text.Text, N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>
        extends FxShapePeer<FxN, N, NB, NM>
        implements TextPeerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxTextPeer() {
        this((NB) new TextPeerBase());
    }

    FxTextPeer(NB base) {
        super(base);
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.text.Text();
    }

    @Override
    public void updateText(String text) {
        getFxNode().setText(text);
    }

    @Override
    public void updateFont(Font font) {
        getFxNode().setFont(font);
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        getFxNode().setTextOrigin(toFxVpos(textOrigin));
    }

    @Override
    public void updateX(Double x) {
        getFxNode().setX(x);
    }

    @Override
    public void updateY(Double y) {
        getFxNode().setY(y);
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        getFxNode().setWrappingWidth(wrappingWidth);
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        getFxNode().setTextAlignment(toFxTextAlignment(textAlignment));
    }

    private static javafx.geometry.VPos toFxVpos(VPos vpos) {
        if (vpos != null)
            switch (vpos) {
                case TOP: return javafx.geometry.VPos.TOP;
                case CENTER: return javafx.geometry.VPos.CENTER;
                case BASELINE: return javafx.geometry.VPos.BASELINE;
                case BOTTOM: return javafx.geometry.VPos.BOTTOM;
            }
        return null;
    }

    private static javafx.scene.text.TextAlignment toFxTextAlignment(TextAlignment textAlignment) {
        if (textAlignment != null)
            switch (textAlignment) {
                case LEFT: return javafx.scene.text.TextAlignment.LEFT;
                case CENTER: return javafx.scene.text.TextAlignment.CENTER;
                case RIGHT: return javafx.scene.text.TextAlignment.RIGHT;
                case JUSTIFY: return javafx.scene.text.TextAlignment.JUSTIFY;
            }
        return null;
    }

}
