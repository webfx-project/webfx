package naga.fx.spi.peer.base;

import naga.fx.scene.Node;
import naga.fx.scene.control.Labeled;
import naga.fx.scene.paint.Paint;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.TextAlignment;

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
