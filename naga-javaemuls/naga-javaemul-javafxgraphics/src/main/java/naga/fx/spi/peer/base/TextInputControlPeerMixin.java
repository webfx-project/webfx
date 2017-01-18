package naga.fx.spi.peer.base;

import emul.javafx.scene.control.TextInputControl;
import emul.javafx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public interface TextInputControlPeerMixin
        <N extends TextInputControl, NB extends TextInputControlPeerBase<N, NB, NM>, NM extends TextInputControlPeerMixin<N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateFont(Font font);

    void updateText(String text);

    void updatePrompt(String prompt);
}
