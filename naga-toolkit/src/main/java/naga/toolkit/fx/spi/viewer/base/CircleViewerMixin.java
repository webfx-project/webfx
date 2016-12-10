package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.viewer.CircleViewer;

/**
 * @author Bruno Salmon
 */
public interface CircleViewerMixin
        extends CircleViewer,
        ShapeViewerMixin<Circle, CircleViewerBase, CircleViewerMixin> {

    void updateCenterX(Double centerX);

    void updateCenterY(Double centerY);

    void updateRadius(Double radius);
}
