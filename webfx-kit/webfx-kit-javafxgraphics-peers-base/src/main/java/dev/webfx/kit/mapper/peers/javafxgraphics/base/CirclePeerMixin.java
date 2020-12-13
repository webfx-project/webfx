package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.scene.shape.Circle;

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
