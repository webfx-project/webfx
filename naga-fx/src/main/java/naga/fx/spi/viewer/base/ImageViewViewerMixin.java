package naga.fx.spi.viewer.base;

import naga.fx.scene.image.Image;
import naga.fx.scene.image.ImageView;

/**
 * @author Bruno Salmon
 */
public interface ImageViewViewerMixin
        <N extends ImageView, NB extends ImageViewViewerBase<N, NB, NM>, NM extends ImageViewViewerMixin<N, NB, NM>>

        extends NodeViewerMixin<N, NB, NM> {

    void updateImage(Image image);

    void updateFitWidth(Double fitWidth);

    void updateFitHeight(Double fitHeight);

    void updateX(Double x);

    void updateY(Double y);
}
