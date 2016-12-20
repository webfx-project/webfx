package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.viewer.base.CircleViewerBase;
import naga.toolkit.fx.spi.viewer.base.CircleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxCircleViewer
        <FxN extends javafx.scene.shape.Circle, N extends Circle, NV extends CircleViewerBase<N, NV, NM>, NM extends CircleViewerMixin<N, NV, NM>>

        extends FxShapeViewer<FxN, N, NV, NM>
        implements CircleViewerMixin<N, NV, NM> {

    public FxCircleViewer() {
        super((NV) new CircleViewerBase());
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
