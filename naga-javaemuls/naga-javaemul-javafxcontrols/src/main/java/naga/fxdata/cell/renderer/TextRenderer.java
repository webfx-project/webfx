package naga.fxdata.cell.renderer;

import emul.javafx.scene.text.Text;
import naga.util.Strings;

/**
 * @author Bruno Salmon
 */
public class TextRenderer implements ValueRenderer {

    public static TextRenderer SINGLETON = new TextRenderer();

    private TextRenderer() {}

    @Override
    public Text renderValue(Object value) {
        return new Text(Strings.toSafeString(value));
    }
}
