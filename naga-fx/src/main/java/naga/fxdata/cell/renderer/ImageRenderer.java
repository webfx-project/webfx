package naga.fxdata.cell.renderer;

import javafx.scene.image.ImageView;
import naga.commons.util.Strings;
import naga.fx.util.ImageStore;

/**
 * @author Bruno Salmon
 */
public class ImageRenderer implements ValueRenderer {

    public static ImageRenderer SINGLETON = new ImageRenderer();

    private ImageRenderer() {}

    @Override
    public ImageView renderCellValue(Object value) {
        return ImageStore.createImageView(Strings.toString(value));
    }
}
