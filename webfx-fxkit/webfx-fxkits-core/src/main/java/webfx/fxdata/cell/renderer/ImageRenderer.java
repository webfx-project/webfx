package webfx.fxdata.cell.renderer;

import javafx.scene.image.ImageView;
import webfx.util.Strings;
import webfx.fx.util.ImageStore;

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
