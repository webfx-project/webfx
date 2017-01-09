package naga.fx.spi.javafx.peer;

import naga.fx.scene.shape.Rectangle;
import naga.fx.spi.peer.base.RectanglePeerBase;
import naga.fx.spi.peer.base.RectanglePeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxRectanglePeer
        <FxN extends javafx.scene.shape.Rectangle, N extends Rectangle, NB extends RectanglePeerBase<N, NB, NM>, NM extends RectanglePeerMixin<N, NB, NM>>

        extends FxShapePeer<FxN, N, NB, NM>
        implements RectanglePeerMixin<N, NB, NM> {

    public FxRectanglePeer() {
        super((NB) new RectanglePeerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.shape.Rectangle();
    }

    @Override
    public void updateX(Double x) {
        getFxNode().setX(x);
    }

    @Override
    public void updateY(Double y) {
        getFxNode().setY(y);
    }

    @Override
    public void updateWidth(Double width) {
        getFxNode().setWidth(width);
    }

    @Override
    public void updateHeight(Double height) {
        getFxNode().setHeight(height);
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        getFxNode().setArcWidth(arcWidth);
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        getFxNode().setArcHeight(arcHeight);
    }
}
