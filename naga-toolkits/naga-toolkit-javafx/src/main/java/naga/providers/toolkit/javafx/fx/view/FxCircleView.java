package naga.providers.toolkit.javafx.fx.view;

import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.view.base.CircleViewBase;
import naga.toolkit.fx.spi.view.base.CircleViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxCircleView
        extends FxShapeView<javafx.scene.shape.Circle, Circle, CircleViewBase, CircleViewMixin>
        implements CircleViewMixin {

    public FxCircleView() {
        super(new CircleViewBase());
    }

    @Override
    javafx.scene.shape.Circle createFxNode() {
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
