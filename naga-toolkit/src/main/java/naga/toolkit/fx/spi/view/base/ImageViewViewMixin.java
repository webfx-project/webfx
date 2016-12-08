package naga.toolkit.fx.spi.view.base;

import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.view.ImageViewView;

/**
 * @author Bruno Salmon
 */
public interface ImageViewViewMixin
        extends ImageViewView,
        NodeViewMixin<ImageView, ImageViewViewBase, ImageViewViewMixin> {

    void updateImageUrl(String imageUrl);
}
