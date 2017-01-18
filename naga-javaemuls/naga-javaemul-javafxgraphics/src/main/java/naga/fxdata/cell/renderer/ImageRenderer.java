package naga.fxdata.cell.renderer;

import emul.javafx.scene.image.ImageView;
import naga.commons.util.Strings;

/**
 * @author Bruno Salmon
 */
class ImageRenderer implements ValueRenderer {

    public static ImageRenderer SINGLETON = new ImageRenderer();

    private ImageRenderer() {}

    @Override
    public ImageView renderCellValue(Object value) {
        return new ImageView(Strings.toString(value));
    }
}
