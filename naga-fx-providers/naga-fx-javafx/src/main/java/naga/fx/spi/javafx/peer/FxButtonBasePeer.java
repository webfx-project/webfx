package naga.fx.spi.javafx.peer;

import naga.fx.scene.control.ButtonBase;
import naga.fx.spi.peer.base.ButtonBasePeerBase;
import naga.fx.spi.peer.base.ButtonBasePeerMixin;

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
