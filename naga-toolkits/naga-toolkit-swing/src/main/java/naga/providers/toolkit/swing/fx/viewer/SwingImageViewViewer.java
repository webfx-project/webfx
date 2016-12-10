package naga.providers.toolkit.swing.fx.viewer;

import naga.providers.toolkit.swing.util.JGradientLabel;
import naga.providers.toolkit.swing.util.SwingImageStore;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.viewer.base.ImageViewViewerBase;
import naga.toolkit.fx.spi.viewer.base.ImageViewViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SwingImageViewViewer
        extends SwingNodeViewer<ImageView, ImageViewViewerBase, ImageViewViewerMixin>
        implements ImageViewViewerMixin, SwingLayoutMeasurable<ImageView> {

    private final JGradientLabel swingImage = new JGradientLabel();

    public SwingImageViewViewer() {
        super(new ImageViewViewerBase());
    }

    @Override
    public void updateImageUrl(String imageUrl) {
        ImageView imageView = getNode();
        swingImage.setIcon(SwingImageStore.getIcon(imageUrl, imageView.getFitWidth().intValue(), imageView.getFitHeight().intValue()));
    }

    @Override
    public JGradientLabel getSwingComponent() {
        return swingImage;
    }
}
