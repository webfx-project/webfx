package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.image.ImageView;

/**
 * @author Bruno Salmon
 */
public interface ImageViewViewerMixin
        <N extends ImageView, NV extends ImageViewViewerBase<N, NV, NM>, NM extends ImageViewViewerMixin<N, NV, NM>>

        extends NodeViewerMixin<N, NV, NM> {

    void updateImageUrl(String imageUrl);

    void updateFitWidth(Double fitWidth);

    void updateFitHeight(Double fitHeight);
}
