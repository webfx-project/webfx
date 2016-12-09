package naga.toolkit.fx.ext.cell.renderer;

import naga.commons.util.Strings;
import naga.toolkit.fx.scene.Node;
import naga.toolkit.fx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
public class HtmlRenderer implements ValueRenderer {

    public static HtmlRenderer SINGLETON = new HtmlRenderer();

    private HtmlRenderer() {}

    @Override
    public Node renderCellValue(Object value) {
        return Text.create(Strings.toString(value));
    }
}
