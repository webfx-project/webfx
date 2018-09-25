package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.Group;
import webfx.fxkits.core.mapper.spi.impl.peer.GroupPeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.GroupPeerMixin;

/**
 * @author Bruno Salmon
 */
public final class FxGroupPeer
        <FxN extends javafx.scene.Group, N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends FxNodePeer<FxN, N, NB, NM>
        implements GroupPeerMixin<N, NB, NM> {

    public FxGroupPeer() {
        super((NB) new GroupPeerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.Group();
    }
}
