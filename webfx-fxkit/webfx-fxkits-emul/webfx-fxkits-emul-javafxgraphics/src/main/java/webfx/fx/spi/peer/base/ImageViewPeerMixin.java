package webfx.fx.spi.peer.base;

import emul.javafx.scene.image.Image;
import emul.javafx.scene.image.ImageView;

/**
 * @author Bruno Salmon
 */
public interface ImageViewPeerMixin
        <N extends ImageView, NB extends ImageViewPeerBase<N, NB, NM>, NM extends ImageViewPeerMixin<N, NB, NM>>

        extends NodePeerMixin<N, NB, NM> {

    void updateImage(Image image);

    void updateFitWidth(Double fitWidth);

    void updateFitHeight(Double fitHeight);

    void updateX(Double x);

    void updateY(Double y);
}
