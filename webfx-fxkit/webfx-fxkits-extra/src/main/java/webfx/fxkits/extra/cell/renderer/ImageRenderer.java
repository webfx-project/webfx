package webfx.fxkits.extra.cell.renderer;

import javafx.scene.image.ImageView;
import webfx.platforms.core.util.Strings;
import webfx.fxkits.extra.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class ImageRenderer implements ValueRenderer {

    public final static ImageRenderer SINGLETON = new ImageRenderer();

    private ImageRenderer() {}

    @Override
    public ImageView renderValue(Object value, ValueRenderingContext context) {
        return ImageStore.createImageView(Strings.toString(value));
    }
}
