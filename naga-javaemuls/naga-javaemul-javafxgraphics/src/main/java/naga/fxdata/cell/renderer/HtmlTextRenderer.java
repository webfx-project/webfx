package naga.fxdata.cell.renderer;

import emul.javafx.scene.Node;
import naga.commons.util.Strings;
import naga.fxdata.control.HtmlText;

/**
 * @author Bruno Salmon
 */
public class HtmlTextRenderer implements ValueRenderer {

    public static HtmlTextRenderer SINGLETON = new HtmlTextRenderer();

    private HtmlTextRenderer() {}

    @Override
    public Node renderCellValue(Object value) {
        return new HtmlText(Strings.toString(value));
    }
}
