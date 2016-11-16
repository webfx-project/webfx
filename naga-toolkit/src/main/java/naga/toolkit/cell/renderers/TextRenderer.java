package naga.toolkit.cell.renderers;

import naga.commons.util.Strings;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class TextRenderer implements ValueRenderer {

    public static TextRenderer SINGLETON = new TextRenderer();

    private TextRenderer() {}

    @Override
    public GuiNode renderCellValue(Object value) {
        return Toolkit.get().createTextView(Strings.toString(value));
    }
}
