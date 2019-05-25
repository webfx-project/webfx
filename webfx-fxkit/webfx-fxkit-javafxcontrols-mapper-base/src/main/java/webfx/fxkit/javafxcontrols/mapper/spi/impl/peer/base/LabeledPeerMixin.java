package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base;

import javafx.scene.Node;
import javafx.scene.control.Labeled;
import javafx.scene.paint.Paint;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public interface LabeledPeerMixin
        <N extends Labeled, NB extends LabeledPeerBase<N, NB, NM>, NM extends LabeledPeerMixin<N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateText(String text);

    void updateGraphic(Node graphic);

    void updateFont(Font font);

    void updateTextAlignment(TextAlignment textAlignment);

    void updateTextFill(Paint textFill);
}
