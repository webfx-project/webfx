package naga.fxdata.cell.renderer;

import emul.javafx.scene.text.Text;
import naga.commons.util.Strings;

/**
 * @author Bruno Salmon
 */
public class TextRenderer implements ValueRenderer {

    public static TextRenderer SINGLETON = new TextRenderer();

    private TextRenderer() {}

    @Override
    public Text renderCellValue(Object value) {
        return new Text(Strings.toSafeString(value));
    }
}
