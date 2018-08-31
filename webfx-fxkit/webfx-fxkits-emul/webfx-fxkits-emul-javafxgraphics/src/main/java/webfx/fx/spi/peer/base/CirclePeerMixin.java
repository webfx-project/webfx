package webfx.fx.spi.peer.base;

import emul.javafx.scene.shape.Circle;

/**
 * @author Bruno Salmon
 */
public interface CirclePeerMixin
        <N extends Circle, NB extends CirclePeerBase<N, NB, NM>, NM extends CirclePeerMixin<N, NB, NM>>

        extends ShapePeerMixin<N, NB, NM> {

    void updateCenterX(Double centerX);

    void updateCenterY(Double centerY);

    void updateRadius(Double radius);
}
