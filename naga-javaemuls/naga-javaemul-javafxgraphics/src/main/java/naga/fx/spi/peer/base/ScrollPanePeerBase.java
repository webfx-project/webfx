package naga.fx.spi.peer.base;

import emul.javafx.scene.control.ScrollPane;

/**
 * @author Bruno Salmon
 */
public class ScrollPanePeerBase
        <N extends ScrollPane, NB extends ScrollPanePeerBase<N, NB, NM>, NM extends ScrollPanePeerMixin<N, NB, NM>>

        extends RegionPeerBase<N, NB, NM> {
}
