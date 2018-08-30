package naga.fx.spi.peer.base;

import emul.javafx.scene.control.RadioButton;

/**
 * @author Bruno Salmon
 */
public interface RadioButtonPeerMixin
        <N extends RadioButton, NB extends RadioButtonPeerBase<N, NB, NM>, NM extends RadioButtonPeerMixin<N, NB, NM>>

        extends ToggleButtonPeerMixin<N, NB, NM> {
}
