package webfx.fxkits.extra.cell.renderer;

import webfx.fxkits.extra.control.HtmlText;
import webfx.platforms.core.util.Strings;

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
