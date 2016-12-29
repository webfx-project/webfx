package naga.fx.spi.viewer.base;

import naga.fx.scene.shape.Circle;

/**
 * @author Bruno Salmon
 */
public interface CircleViewerMixin
        <N extends Circle, NB extends CircleViewerBase<N, NB, NM>, NM extends CircleViewerMixin<N, NB, NM>>

        extends ShapeViewerMixin<N, NB, NM> {

    void updateCenterX(Double centerX);

    void updateCenterY(Double centerY);

    void updateRadius(Double radius);
}
