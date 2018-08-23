package naga.fx.spi.peer.base;

import emul.javafx.scene.control.ToggleButton;

/**
 * @author Bruno Salmon
 */
public interface ToggleButtonPeerMixin
        <N extends ToggleButton, NB extends ToggleButtonPeerBase<N, NB, NM>, NM extends ToggleButtonPeerMixin<N, NB, NM>>

        extends ButtonBasePeerMixin<N, NB, NM> {

    void updateSelected(Boolean selected);
}
