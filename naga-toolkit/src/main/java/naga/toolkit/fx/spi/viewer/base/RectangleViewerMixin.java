package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.shape.Rectangle;

/**
 * @author Bruno Salmon
 */
public interface RectangleViewerMixin
        <N extends Rectangle, NV extends RectangleViewerBase<N, NV, NM>, NM extends RectangleViewerMixin<N, NV, NM>>

        extends ShapeViewerMixin<N, NV, NM> {

    void updateX(Double x);

    void updateY(Double y);

    void updateWidth(Double width);

    void updateHeight(Double height);

    void updateArcWidth(Double arcWidth);

    void updateArcHeight(Double arcHeight);
}
