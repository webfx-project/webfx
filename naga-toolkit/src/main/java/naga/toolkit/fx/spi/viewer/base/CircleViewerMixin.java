package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.shape.Circle;

/**
 * @author Bruno Salmon
 */
public interface CircleViewerMixin
        <N extends Circle, NV extends CircleViewerBase<N, NV, NM>, NM extends CircleViewerMixin<N, NV, NM>>

        extends ShapeViewerMixin<N, NV, NM> {

    void updateCenterX(Double centerX);

    void updateCenterY(Double centerY);

    void updateRadius(Double radius);
}
