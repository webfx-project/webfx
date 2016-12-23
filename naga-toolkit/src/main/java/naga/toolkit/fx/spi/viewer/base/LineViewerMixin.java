package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.shape.Line;

/**
 * @author Bruno Salmon
 */
public interface LineViewerMixin
        <N extends Line, NB extends LineViewerBase<N, NB, NM>, NM extends LineViewerMixin<N, NB, NM>>

        extends ShapeViewerMixin<N, NB, NM> {

    void updateStartX(Double startX);

    void updateStartY(Double startY);

    void updateEndX(Double endX);

    void updateEndY(Double endY);
}
