package webfx.fx.spi.peer.base;

import emul.javafx.scene.Group;

/**
 * @author Bruno Salmon
 */
public interface GroupPeerMixin
        <N extends Group, NB extends GroupPeerBase<N, NB, NM>, NM extends GroupPeerMixin<N, NB, NM>>

        extends NodePeerMixin<N, NB, NM> {
}
