package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.control.TextInputControl;
import webfx.fxkit.mapper.spi.impl.peer.TextInputControlPeerMixin;
import webfx.fxkit.mapper.spi.impl.peer.TextInputControlPeerBase;

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
