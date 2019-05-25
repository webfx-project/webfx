package webfx.fxkit.javafxcontrols.mapper.spi.impl.peer.base;

import javafx.scene.control.Label;

/**
 * @author Bruno Salmon
 */
public class LabelPeerBase
        <N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>

        extends LabeledPeerBase<N, NB, NM> {
}
