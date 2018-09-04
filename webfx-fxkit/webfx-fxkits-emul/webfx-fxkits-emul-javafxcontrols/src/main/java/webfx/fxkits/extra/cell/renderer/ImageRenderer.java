package webfx.fxkits.extra.cell.renderer;

import emul.javafx.scene.image.ImageView;
import webfx.fxkits.extra.util.ImageStore;
import webfx.platforms.core.util.Strings;

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
