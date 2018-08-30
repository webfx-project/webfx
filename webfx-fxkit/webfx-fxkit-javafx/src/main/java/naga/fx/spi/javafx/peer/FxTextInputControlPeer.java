package naga.fx.spi.javafx.peer;

import javafx.scene.control.TextInputControl;
import naga.fx.spi.peer.base.TextInputControlPeerMixin;
import naga.fx.spi.peer.base.TextInputControlPeerBase;

/**
 * @author Bruno Salmon
 */
abstract class FxTextInputControlPeer
        <FxN extends javafx.scene.control.TextInputControl, N extends TextInputControl, NB extends TextInputControlPeerBase<N, NB, NM>, NM extends TextInputControlPeerMixin<N, NB, NM>>
        extends FxControlPeer<FxN, N, NB, NM> {

    FxTextInputControlPeer(NB base) {
        super(base);
    }
}
