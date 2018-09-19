package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.control.ButtonBase;
import webfx.fxkits.core.mapper.spi.impl.peer.ButtonBasePeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.ButtonBasePeerMixin;

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
