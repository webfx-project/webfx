package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.control.Button;
import webfx.fxkits.core.mapper.spi.impl.peer.ButtonPeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.ButtonPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class FxButtonPeer
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
