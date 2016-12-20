package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.image.ImageView;

/**
 * @author Bruno Salmon
 */
public interface ImageViewViewerMixin
        extends NodeViewerMixin<ImageView, ImageViewViewerBase, ImageViewViewerMixin> {

    void updateImageUrl(String imageUrl);

    void updateFitWidth(Double fitWidth);

    void updateFitHeight(Double fitHeight);
}
