package webfx.fxkits.core.spi.peer.base;

import javafx.scene.control.ButtonBase;

/**
 * @author Bruno Salmon
 */
public abstract class ButtonBasePeerBase
        <N extends ButtonBase, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends LabeledPeerBase<N, NB, NM> {
}
