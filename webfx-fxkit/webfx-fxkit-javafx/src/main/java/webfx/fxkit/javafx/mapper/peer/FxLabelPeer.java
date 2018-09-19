package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.control.Label;
import webfx.fxkits.core.mapper.spi.impl.peer.LabelPeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.LabelPeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxLabelPeer
        <FxN extends javafx.scene.control.Label, N extends Label, NB extends LabelPeerBase<N, NB, NM>, NM extends LabelPeerMixin<N, NB, NM>>

        extends FxLabeledPeer<FxN, N, NB, NM>
        implements LabelPeerMixin<N, NB, NM>, FxLayoutMeasurable {

    public FxLabelPeer() {
        this((NB) new LabelPeerBase());
    }

    FxLabelPeer(NB base) {
        super(base);
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.control.Label();
    }

}
