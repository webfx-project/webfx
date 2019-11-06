package webfx.kit.mapper.peers.javafxgraphics.base;

import javafx.scene.canvas.Canvas;

/**
 * @author Bruno Salmon
 */
public interface CanvasPeerMixin
        <N extends Canvas, NB extends CanvasPeerBase<N, NB, NM>, NM extends CanvasPeerMixin<N, NB, NM>>

        extends NodePeerMixin<N, NB, NM> {

    void updateWidth(Number width);

    void updateHeight(Number height);
}
