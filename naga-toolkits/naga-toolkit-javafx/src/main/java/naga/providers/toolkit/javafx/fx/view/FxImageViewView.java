package naga.providers.toolkit.javafx.fx.view;

import javafx.scene.image.Image;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.view.base.ImageViewViewBase;
import naga.toolkit.fx.spi.view.base.ImageViewViewMixin;

/**
 * @author Bruno Salmon
 */
public class FxImageViewView
        extends FxNodeView<javafx.scene.image.ImageView, ImageView, ImageViewViewBase, ImageViewViewMixin>
        implements ImageViewViewMixin {

    public FxImageViewView() {
        super(new ImageViewViewBase());
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
