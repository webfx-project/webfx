package naga.toolkit.cell.renderers;

import naga.commons.util.Strings;
import naga.toolkit.spi.Toolkit;
import naga.toolkit.spi.nodes.GuiNode;

/**
 * @author Bruno Salmon
 */
public class HtmlRenderer<N> implements ValueRenderer<N> {

    public static HtmlRenderer SINGLETON = new HtmlRenderer();

    private HtmlRenderer() {}

    @Override
    public GuiNode<N> renderCellValue(Object value) {
        return Toolkit.get().createHtmlView(Strings.toString(value));
    }
}
