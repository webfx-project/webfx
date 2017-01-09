package naga.fx.spi.javafx.peer;

import naga.fx.spi.javafx.util.FxImageStore;
import naga.fx.scene.image.Image;
import naga.fx.scene.image.ImageView;
import naga.fx.spi.Toolkit;
import naga.fx.spi.peer.base.ImageViewPeerBase;
import naga.fx.spi.peer.base.ImageViewPeerMixin;

/**
 * @author Bruno Salmon
 */
public class FxImageViewPeer
        <FxN extends javafx.scene.image.ImageView, N extends ImageView, NB extends ImageViewPeerBase<N, NB, NM>, NM extends ImageViewPeerMixin<N, NB, NM>>

        extends FxNodePeer<FxN, N, NB, NM>
        implements ImageViewPeerMixin<N, NB, NM> {

    public FxImageViewPeer() {
        super((NB) new ImageViewPeerBase());
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
