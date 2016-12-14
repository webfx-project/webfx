package naga.providers.toolkit.javafx.fx.viewer;

import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.viewer.base.CircleViewerBase;
import naga.toolkit.fx.spi.viewer.base.CircleViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxCircleViewer
        extends FxShapeViewer<javafx.scene.shape.Circle, Circle, CircleViewerBase, CircleViewerMixin>
        implements CircleViewerMixin {

    public FxCircleViewer() {
        super(new CircleViewerBase());
    }

    @Override
    protected javafx.scene.shape.Circle createFxNode() {
        return new javafx.scene.shape.Circle();
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
