package naga.toolkit.fx.scene.image;

import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.image.impl.ImageViewImpl;
import naga.toolkit.properties.markers.*;

/**
 * @author Bruno Salmon
 */
public interface ImageView extends Node,
        HasImageUrlProperty,
        HasFitWidthProperty,
        HasFitHeightProperty,
        HasXProperty,
        HasYProperty {

    static ImageView create() {
        return new ImageViewImpl();
    }

    static ImageView create(String imageUrl) {
        return new ImageViewImpl(imageUrl);
    }
}
