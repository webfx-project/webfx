package naga.toolkit.fx.ext.cell.renderer;

import naga.commons.util.Strings;
import naga.toolkit.fx.scene.text.Text;

/**
 * @author Bruno Salmon
 */
public class TextRenderer implements ValueRenderer {

    public static TextRenderer SINGLETON = new TextRenderer();

    private TextRenderer() {}

    @Override
    public Text renderCellValue(Object value) {
        return Text.create(Strings.toSafeString(value));
    }
}
