package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.view.CircleView;

/**
 * @author Bruno Salmon
 */
public interface CircleViewMixin
        extends CircleView,
                ShapeViewMixin<Circle, CircleViewBase, CircleViewMixin> {

    void updateCenterX(Double centerX);

    void updateCenterY(Double centerY);

    void updateRadius(Double radius);
}
