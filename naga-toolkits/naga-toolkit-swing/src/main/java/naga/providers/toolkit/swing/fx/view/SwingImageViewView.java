package naga.providers.toolkit.swing.fx.view;

import naga.providers.toolkit.swing.util.JGradientLabel;
import naga.providers.toolkit.swing.util.SwingImageStore;
import naga.toolkit.fx.scene.image.ImageView;
import naga.toolkit.fx.spi.view.base.ImageViewViewBase;
import naga.toolkit.fx.spi.view.base.ImageViewViewMixin;

/**
 * @author Bruno Salmon
 */
public class SwingImageViewView
        extends SwingNodeView<ImageView, ImageViewViewBase, ImageViewViewMixin>
        implements ImageViewViewMixin, SwingLayoutMeasurable<ImageView> {

    private final JGradientLabel swingImage = new JGradientLabel();

    public SwingImageViewView() {
        super(new ImageViewViewBase());
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
