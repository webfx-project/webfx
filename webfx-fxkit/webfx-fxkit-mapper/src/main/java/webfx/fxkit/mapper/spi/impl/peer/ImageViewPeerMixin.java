package webfx.fxkit.mapper.spi.impl.peer;

import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

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
