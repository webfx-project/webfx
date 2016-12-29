package naga.fxdata.cell.renderer;

import naga.commons.util.Strings;
import naga.fx.scene.image.ImageView;

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
