package naga.toolkit.cell.renderers;

import naga.commons.util.Strings;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
class ImageCellRenderer<N> implements CellRenderer<N> {

    public static ImageCellRenderer SINGLETON = new ImageCellRenderer();

    private ImageCellRenderer() {}

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        return Toolkit.get().createImage(Strings.toString(value));
    }
}
