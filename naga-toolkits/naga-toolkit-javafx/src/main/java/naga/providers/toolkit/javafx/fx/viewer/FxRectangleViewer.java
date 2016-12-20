package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.spi.viewer.base.RectangleViewerBase;
import naga.toolkit.fx.spi.viewer.base.RectangleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxRectangleViewer
        <FxN extends javafx.scene.shape.Rectangle, N extends Rectangle, NV extends RectangleViewerBase<N, NV, NM>, NM extends RectangleViewerMixin<N, NV, NM>>

        extends FxShapeViewer<FxN, N, NV, NM>
        implements RectangleViewerMixin<N, NV, NM> {

    public FxRectangleViewer() {
        super((NV) new RectangleViewerBase());
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
