package naga.fx.spi.javafx.fx.viewer;

import naga.fx.scene.shape.Rectangle;
import naga.fx.spi.viewer.base.RectangleViewerBase;
import naga.fx.spi.viewer.base.RectangleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxRectangleViewer
        <FxN extends javafx.scene.shape.Rectangle, N extends Rectangle, NB extends RectangleViewerBase<N, NB, NM>, NM extends RectangleViewerMixin<N, NB, NM>>

        extends FxShapeViewer<FxN, N, NB, NM>
        implements RectangleViewerMixin<N, NB, NM> {

    public FxRectangleViewer() {
        super((NB) new RectangleViewerBase());
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
