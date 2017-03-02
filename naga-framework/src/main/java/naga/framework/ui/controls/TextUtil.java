package naga.framework.ui.controls;

import javafx.scene.text.Text;
import naga.framework.ui.i18n.I18n;

/**
 * @author Bruno Salmon
 */
public class TextUtil {

    public static Text createText(String translationKey, I18n i18n) {
        return i18n.translateText(new Text(), translationKey);
    }
}
