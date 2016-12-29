package naga.fx.spi.javafx.fx.viewer;

import naga.fx.spi.javafx.util.FxImageStore;
import naga.fx.scene.image.Image;
import naga.fx.scene.image.ImageView;
import naga.fx.spi.Toolkit;
import naga.fx.spi.viewer.base.ImageViewViewerBase;
import naga.fx.spi.viewer.base.ImageViewViewerMixin;

/**
 * @author Bruno Salmon
 */
public class FxImageViewViewer
        <FxN extends javafx.scene.image.ImageView, N extends ImageView, NB extends ImageViewViewerBase<N, NB, NM>, NM extends ImageViewViewerMixin<N, NB, NM>>

        extends FxNodeViewer<FxN, N, NB, NM>
        implements ImageViewViewerMixin<N, NB, NM> {

    public FxImageViewViewer() {
        super((NB) new ImageViewViewerBase());
    }

    @Override
    protected FxN createFxNode() {
        return (FxN) new javafx.scene.image.ImageView();
    }

    @Override
    public void updateImage(Image image) {
        // As we are probably in the animation frame we make this call non blocking by either setting the image
        // immediately if it is already loaded and present in cache, or deferring the image load in the background
        double fitWidth = getNode().getFitWidth();
        double fitHeight = getNode().getFitHeight();
        String imageUrl = image == null ? null : image.getUrl();
        javafx.scene.image.Image cachedImage = FxImageStore.getImageFromCache(imageUrl, fitWidth, fitHeight);
        if (cachedImage != null || imageUrl == null)
            getFxNode().setImage(cachedImage); // Setting the image immediately
        else // Deferring the image load in the background because it can take more than 16ms (breaking 60 FPS)
            Toolkit.get().scheduler().runInBackground(() -> {
                // Loading the image
                javafx.scene.image.Image fxImage = FxImageStore.getImage(imageUrl, fitWidth, fitHeight);
                // Now that we have the image, we update the JavaFx node (in the UI thread)
                Toolkit.get().scheduler().runInUiThread(() -> getFxNode().setImage(fxImage));
            });
    }

    @Override
    public void updateFitWidth(Double fitWidth) {
        getFxNode().setFitWidth(fitWidth);
    }

    @Override
    public void updateFitHeight(Double fitHeight) {
        getFxNode().setFitHeight(fitHeight);
    }

    @Override
    public void updateX(Double x) {
        getFxNode().setX(x);
    }

    @Override
    public void updateY(Double y) {
        getFxNode().setY(y);
    }
}
