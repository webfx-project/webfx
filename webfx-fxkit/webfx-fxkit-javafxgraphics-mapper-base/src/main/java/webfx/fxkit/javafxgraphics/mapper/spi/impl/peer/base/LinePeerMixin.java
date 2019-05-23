package webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.base;

import javafx.scene.shape.Line;

/**
 * @author Bruno Salmon
 */
public interface LinePeerMixin
        <N extends Line, NB extends LinePeerBase<N, NB, NM>, NM extends LinePeerMixin<N, NB, NM>>

        extends ShapePeerMixin<N, NB, NM> {

    void updateStartX(Double startX);

    void updateStartY(Double startY);

    void updateEndX(Double endX);

    void updateEndY(Double endY);
}
