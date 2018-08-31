package webfx.fx.spi.peer.base;

import emul.javafx.scene.control.Label;

/**
 * @author Bruno Salmon
 */
public interface LabelPeerMixin
        <N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>

        extends LabeledPeerMixin<N, NB, NM> {

}
