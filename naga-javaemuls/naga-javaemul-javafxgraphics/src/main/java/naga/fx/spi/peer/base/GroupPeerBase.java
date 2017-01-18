package naga.fx.spi.peer.base;

import emul.javafx.scene.Group;

/**
 * @author Bruno Salmon
 */
public class GroupPeerBase
        <N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends NodePeerBase<N, NB, NM> {
}
