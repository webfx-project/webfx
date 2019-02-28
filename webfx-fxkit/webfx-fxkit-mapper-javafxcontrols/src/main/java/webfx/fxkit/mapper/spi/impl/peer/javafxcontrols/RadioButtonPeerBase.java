package webfx.fxkit.mapper.spi.impl.peer.javafxcontrols;

import javafx.scene.control.RadioButton;

/**
 * @author Bruno Salmon
 */
public class RadioButtonPeerBase
        <N extends RadioButton, NB extends RadioButtonPeerBase<N, NB, NM>, NM extends RadioButtonPeerMixin<N, NB, NM>>

        extends ToggleButtonPeerBase<N, NB, NM> {
}
