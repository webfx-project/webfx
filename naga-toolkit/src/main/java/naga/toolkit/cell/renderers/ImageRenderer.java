package naga.toolkit.cell.renderers;

import naga.commons.util.Strings;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
class ImageRenderer<N> implements ValueRenderer<N> {

    public static ImageRenderer SINGLETON = new ImageRenderer();

    private ImageRenderer() {}

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        return Toolkit.get().createImage(Strings.toString(value));
    }
}
