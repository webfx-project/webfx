package webfx.fxkits.core.mapper.spi.impl.peer;

import javafx.scene.control.Label;

/**
 * @author Bruno Salmon
 */
public interface LabelPeerMixin
        <N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>

        extends LabeledPeerMixin<N, NB, NM> {

}
