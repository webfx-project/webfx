package naga.fx.spi.javafx.peer;

import naga.fx.spi.javafx.util.FxFonts;
import naga.fx.spi.javafx.util.FxPaints;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import naga.fx.spi.peer.base.LabeledPeerBase;
import naga.fx.spi.peer.base.LabeledPeerMixin;

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
        getFxNode().setGraphic(toFxNode(graphic));
    }

    @Override
    public void updateFont(Font font) {
        if (font != null)
            getFxNode().setFont(FxFonts.toFxFont(font));
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        getFxNode().setTextAlignment(toFxTextAlignment(textAlignment));
    }

    @Override
    public void updateTextFill(Paint textFill) {
        getFxNode().setTextFill(FxPaints.toFxPaint(textFill));
    }

    private static javafx.geometry.Pos toFxPos(Pos pos) {
        if (pos != null)
            switch (pos) {
                case TOP_LEFT: return javafx.geometry.Pos.TOP_LEFT;
                case TOP_CENTER: return javafx.geometry.Pos.TOP_CENTER;
                case TOP_RIGHT: return javafx.geometry.Pos.TOP_RIGHT;
                case CENTER_LEFT: return javafx.geometry.Pos.CENTER_RIGHT;
                case CENTER: return javafx.geometry.Pos.CENTER;
                case CENTER_RIGHT: return javafx.geometry.Pos.CENTER_RIGHT;
                case BASELINE_LEFT: return javafx.geometry.Pos.BASELINE_LEFT;
                case BASELINE_CENTER: return javafx.geometry.Pos.BASELINE_CENTER;
                case BASELINE_RIGHT: return javafx.geometry.Pos.BASELINE_RIGHT;
                case BOTTOM_LEFT: return javafx.geometry.Pos.BOTTOM_LEFT;
                case BOTTOM_CENTER: return javafx.geometry.Pos.BOTTOM_CENTER;
                case BOTTOM_RIGHT: return javafx.geometry.Pos.BOTTOM_RIGHT;
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
