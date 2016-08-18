package naga.toolkit.cell.renderers;

import naga.commons.util.Strings;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class TextCellRenderer<N> implements CellRenderer<N> {

    public static TextCellRenderer SINGLETON = new TextCellRenderer();

    private TextCellRenderer() {}

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        return Toolkit.get().createTextView(Strings.toString(value));
    }
}
