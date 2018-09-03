package webfx.fxkit.javafx.peer;

import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import webfx.fxkits.core.spi.peer.base.LabeledPeerBase;
import webfx.fxkits.core.spi.peer.base.LabeledPeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxLabeledPeer
        <FxN extends javafx.scene.control.Labeled, N extends Labeled, NB extends LabeledPeerBase<N, NB, NM>, NM extends LabeledPeerMixin<N, NB, NM>>

        extends FxControlPeer<FxN, N, NB, NM>
        implements LabeledPeerMixin<N, NB, NM> {

    FxLabeledPeer(NB base) {
        super(base);
    }

    @Override
    public void updateText(String text) {
        getFxNode().setText(text);
    }

    @Override
    public void updateGraphic(Node graphic) {
        getFxNode().setGraphic(graphic);
    }

    @Override
    public void updateFont(Font font) {
        if (font != null)
            getFxNode().setFont(font);
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        getFxNode().setTextAlignment(textAlignment);
    }

    @Override
    public void updateTextFill(Paint textFill) {
        getFxNode().setTextFill(textFill);
    }

}
