package naga.fx.spi.javafx.peer;

import naga.fx.scene.Group;
import naga.fx.spi.peer.base.GroupPeerBase;
import naga.fx.spi.peer.base.GroupPeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxGroupPeer
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
