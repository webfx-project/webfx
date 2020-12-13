package dev.webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.scene.shape.Arc;
import javafx.scene.shape.ArcType;

/**
 * @author Bruno Salmon
 */
public interface ArcPeerMixin
        <N extends Arc, NB extends ArcPeerBase<N, NB, NM>, NM extends ArcPeerMixin<N, NB, NM>>

        extends ShapePeerMixin<N, NB, NM> {

    void updateType(ArcType arcType);

    void updateCenterX(Double centerX);

    void updateCenterY(Double centerY);

    void updateRadiusX(Double radiusX);

    void updateRadiusY(Double radiusY);

    void updateStartAngle(Double startAngle);

    void updateLength(Double length);
}
