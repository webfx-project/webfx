package naga.providers.toolkit.javafx.fx.viewer;

import javafx.scene.image.Image;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.viewer.base.ImageViewViewerBase;
import naga.toolkit.fx.spi.viewer.base.ImageViewViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxImageViewViewer
        extends FxNodeViewer<javafx.scene.image.ImageView, ImageView, ImageViewViewerBase, ImageViewViewerMixin>
        implements ImageViewViewerMixin {

    public FxImageViewViewer() {
        super(new ImageViewViewerBase());
    }

    @Override
    javafx.scene.image.ImageView createFxNode() {
        return new javafx.scene.image.ImageView();
    }

    @Override
    public void updateImageUrl(String imageUrl) {
        getFxNode().setImage(imageUrl == null ? null : new Image(imageUrl));
    }
}
