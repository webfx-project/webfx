package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.Circle;
import naga.toolkit.drawing.spi.view.CircleView;

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
