package naga.fx.spi.javafx.peer;

import javafx.scene.control.Button;
import naga.fx.spi.peer.base.ButtonPeerBase;
import naga.fx.spi.peer.base.ButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxButtonPeer
        <FxN extends javafx.scene.control.Button, N extends Button, NB extends ButtonPeerBase<N, NB, NM>, NM extends ButtonPeerMixin<N, NB, NM>>

        extends FxButtonBasePeer<FxN, N, NB, NM>
        implements ButtonPeerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxButtonPeer() {
        super((NB) new ButtonPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.control.Button();
    }
}
