package naga.fx.spi.viewer.base;

import naga.fx.scene.shape.Rectangle;

/**
 * @author Bruno Salmon
 */
public interface RectangleViewerMixin
        <N extends Rectangle, NB extends RectangleViewerBase<N, NB, NM>, NM extends RectangleViewerMixin<N, NB, NM>>

        extends ShapeViewerMixin<N, NB, NM> {

    void updateX(Double x);

    void updateY(Double y);

    void updateWidth(Double width);

    void updateHeight(Double height);

    void updateArcWidth(Double arcWidth);

    void updateArcHeight(Double arcHeight);
}
