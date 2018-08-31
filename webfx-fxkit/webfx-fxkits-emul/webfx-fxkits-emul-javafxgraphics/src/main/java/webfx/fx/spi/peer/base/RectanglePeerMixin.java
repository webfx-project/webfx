package webfx.fx.spi.peer.base;

import emul.javafx.scene.shape.Rectangle;

/**
 * @author Bruno Salmon
 */
public interface RectanglePeerMixin
        <N extends Rectangle, NB extends RectanglePeerBase<N, NB, NM>, NM extends RectanglePeerMixin<N, NB, NM>>

        extends ShapePeerMixin<N, NB, NM> {

    void updateX(Double x);

    void updateY(Double y);

    void updateWidth(Double width);

    void updateHeight(Double height);

    void updateArcWidth(Double arcWidth);

    void updateArcHeight(Double arcHeight);
}
