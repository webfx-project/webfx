package webfx.fxkit.mapper.spi.impl.peer.javafxcontrols;

import javafx.scene.control.TextInputControl;
import javafx.scene.text.Font;

/**
 * @author Bruno Salmon
 */
public interface TextInputControlPeerMixin
        <N extends TextInputControl, NB extends TextInputControlPeerBase<N, NB, NM>, NM extends TextInputControlPeerMixin<N, NB, NM>>

        extends ControlPeerMixin<N, NB, NM> {

    void updateFont(Font font);

    void updateText(String text);

    void updatePrompt(String prompt);

    void updateEditable(Boolean editable);
}
