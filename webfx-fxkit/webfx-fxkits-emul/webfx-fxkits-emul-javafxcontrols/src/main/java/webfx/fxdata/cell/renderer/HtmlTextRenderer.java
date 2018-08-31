package webfx.fxdata.cell.renderer;

import webfx.fxdata.control.HtmlText;
import webfx.util.Strings;

/**
 * @author Bruno Salmon
 */
public class HtmlTextRenderer implements ValueRenderer {

    public final static HtmlTextRenderer SINGLETON = new HtmlTextRenderer();

    private HtmlTextRenderer() {}

    @Override
    public HtmlText renderValue(Object value, ValueRenderingContext context) {
        return new HtmlText(Strings.toString(value));
    }
}
