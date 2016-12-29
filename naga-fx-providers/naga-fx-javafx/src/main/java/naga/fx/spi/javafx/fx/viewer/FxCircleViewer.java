package naga.fx.spi.javafx.fx.viewer;

import naga.fx.scene.shape.Circle;
import naga.fx.spi.viewer.base.CircleViewerBase;
import naga.fx.spi.viewer.base.CircleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxCircleViewer
        <FxN extends javafx.scene.shape.Circle, N extends Circle, NB extends CircleViewerBase<N, NB, NM>, NM extends CircleViewerMixin<N, NB, NM>>

        extends FxShapeViewer<FxN, N, NB, NM>
        implements CircleViewerMixin<N, NB, NM> {

    public FxCircleViewer() {
        super((NB) new CircleViewerBase());
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
