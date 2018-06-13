package naga.fxdata.cell.renderer;

import naga.fxdata.control.HtmlText;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
public class HtmlTextRenderer implements ValueRenderer {

    public static HtmlTextRenderer SINGLETON = new HtmlTextRenderer();

    private HtmlTextRenderer() {}

    @Override
    public HtmlText renderValue(Object value, ValueRenderingContext context) {
        return new HtmlText(Strings.toString(value));
    }
}
