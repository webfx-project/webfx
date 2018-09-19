package webfx.fxkit.javafx.mapper.peer;

import javafx.scene.shape.Circle;
import webfx.fxkits.core.mapper.spi.impl.peer.CirclePeerBase;
import webfx.fxkits.core.mapper.spi.impl.peer.CirclePeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxCirclePeer
        <FxN extends javafx.scene.shape.Circle, N extends Circle, NB extends CirclePeerBase<N, NB, NM>, NM extends CirclePeerMixin<N, NB, NM>>

        extends FxShapePeer<FxN, N, NB, NM>
        implements CirclePeerMixin<N, NB, NM> {

    public FxCirclePeer() {
        super((NB) new CirclePeerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.shape.Circle();
    }

    @Override
    public void updateCenterX(Double centerX) {
        getFxNode().setCenterX(centerX);
    }

    @Override
    public void updateCenterY(Double centerY) {
        getFxNode().setCenterY(centerY);
    }

    @Override
    public void updateRadius(Double radius) {
        getFxNode().setRadius(radius);
    }
}
