package naga.toolkit.fx.spi.viewer.base;

import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.viewer.ImageViewViewer;

/**
 * @author Bruno Salmon
 */
public interface ImageViewViewerMixin
        extends ImageViewViewer,
        NodeViewerMixin<ImageView, ImageViewViewerBase, ImageViewViewerMixin> {

    void updateImageUrl(String imageUrl);
}
