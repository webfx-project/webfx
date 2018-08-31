package webfx.fxdata.cell.renderer;

import emul.javafx.scene.image.ImageView;
import webfx.fx.util.ImageStore;
import webfx.util.Strings;

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
