package naga.fx.spi.javafx.peer;

import javafx.scene.shape.Line;
import naga.fx.spi.peer.base.LinePeerMixin;
import naga.fx.spi.peer.base.LinePeerBase;

/**
 * @author Bruno Salmon
 */
public class FxLinePeer
        <FxN extends javafx.scene.shape.Line, N extends Line, NB extends LinePeerBase<N, NB, NM>, NM extends LinePeerMixin<N, NB, NM>>

        extends FxShapePeer<FxN, N, NB, NM>
        implements LinePeerMixin<N, NB, NM> {

    public FxLinePeer() {
        super((NB) new LinePeerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.shape.Line();
    }

    @Override
    public void updateStartX(Double startX) {
        getFxNode().setStartX(startX);
    }

    @Override
    public void updateStartY(Double startY) {
        getFxNode().setStartY(startY);
    }

    @Override
    public void updateEndX(Double endX) {
        getFxNode().setEndX(endX);
    }

    @Override
    public void updateEndY(Double endY) {
        getFxNode().setEndY(endY);
    }
}
