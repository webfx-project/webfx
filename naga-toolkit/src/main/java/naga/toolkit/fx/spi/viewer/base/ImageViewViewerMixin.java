package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.image.ImageView;

/**
 * @author Bruno Salmon
 */
public interface ImageViewViewerMixin
        <N extends ImageView, NB extends ImageViewViewerBase<N, NB, NM>, NM extends ImageViewViewerMixin<N, NB, NM>>

        extends NodeViewerMixin<N, NB, NM> {

    void updateImageUrl(String imageUrl);

    void updateFitWidth(Double fitWidth);

    void updateFitHeight(Double fitHeight);
}
