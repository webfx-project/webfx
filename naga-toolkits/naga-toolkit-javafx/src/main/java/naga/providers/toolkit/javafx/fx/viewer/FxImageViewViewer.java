package naga.providers.toolkit.javafx.fx.viewer;

import naga.providers.toolkit.javafx.util.FxImageStore;
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
    protected javafx.scene.image.ImageView createFxNode() {
        return new javafx.scene.image.ImageView();
    }

    @Override
    public void updateImageUrl(String imageUrl) {
        getFxNode().setImage(FxImageStore.getImage(imageUrl, getNode().getFitWidth().intValue(), getNode().getFitHeight().intValue()));
    }

    @Override
    public void updateFitWidth(Double fitWidth) {
        getFxNode().setFitWidth(fitWidth);
    }

    @Override
    public void updateFitHeight(Double fitHeight) {
        getFxNode().setFitHeight(fitHeight);
    }
}
