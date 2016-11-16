package naga.toolkit.cell.renderers;

import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
class ImageRenderer implements ValueRenderer {

    public static ImageRenderer SINGLETON = new ImageRenderer();

    private ImageRenderer() {}

    @Override
    public GuiNode renderCellValue(Object value) {
        return Toolkit.get().createImage(value);
    }
}
