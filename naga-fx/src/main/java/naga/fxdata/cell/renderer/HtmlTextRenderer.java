package naga.fxdata.cell.renderer;

import naga.util.Strings;
import naga.fxdata.control.HtmlText;
import javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public class HtmlTextRenderer implements ValueRenderer {

    public static HtmlTextRenderer SINGLETON = new HtmlTextRenderer();

    private HtmlTextRenderer() {}

    @Override
    public Node renderValue(Object value) {
        return new HtmlText(Strings.toString(value));
    }
}
