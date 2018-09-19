package webfx.fxkits.core.mapper.spi.impl.peer;

import javafx.scene.control.Label;

/**
 * @author Bruno Salmon
 */
public class LabelPeerBase
        <N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>

        extends LabeledPeerBase<N, NB, NM> {
}
