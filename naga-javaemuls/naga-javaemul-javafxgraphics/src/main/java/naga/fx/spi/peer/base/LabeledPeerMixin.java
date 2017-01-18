package naga.fx.spi.peer.base;

import emul.javafx.scene.Node;
import emul.javafx.scene.control.Labeled;
import emul.javafx.scene.paint.Paint;
import emul.javafx.scene.text.Font;
import emul.javafx.scene.text.TextAlignment;

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
