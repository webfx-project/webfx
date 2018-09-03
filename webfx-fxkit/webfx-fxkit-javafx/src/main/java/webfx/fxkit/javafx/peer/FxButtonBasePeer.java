package webfx.fxkit.javafx.peer;

import javafx.scene.control.ButtonBase;
import webfx.fxkits.core.spi.peer.base.ButtonBasePeerBase;
import webfx.fxkits.core.spi.peer.base.ButtonBasePeerMixin;

/**
 * @author Bruno Salmon
 */
abstract class FxButtonBasePeer
        <FxN extends javafx.scene.control.ButtonBase, N extends ButtonBase, NB extends ButtonBasePeerBase<N, NB, NM>, NM extends ButtonBasePeerMixin<N, NB, NM>>

        extends FxLabeledPeer<FxN, N, NB, NM>
        implements ButtonBasePeerMixin<N, NB, NM> {

    FxButtonBasePeer(NB base) {
        super(base);
    }
}
