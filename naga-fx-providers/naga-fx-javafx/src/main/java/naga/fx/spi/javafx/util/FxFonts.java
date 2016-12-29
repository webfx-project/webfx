package naga.fx.spi.javafx.util;

import naga.fx.scene.text.Font;
import naga.fx.scene.text.FontPosture;
import naga.fx.scene.text.FontWeight;

/**
 * @author Bruno Salmon
 */
public class FxFonts {

    public static javafx.scene.text.Font toFxFont(Font font) {
        return font == null ? null : javafx.scene.text.Font.font(font.getFamily(), toFxFontWeight(font.getWeight()), toFxFontPosture(font.getPosture()), font.getSize());
    }

    private static javafx.scene.text.FontWeight toFxFontWeight(FontWeight weight) {
        return weight == null ? null : javafx.scene.text.FontWeight.findByWeight(weight.getWeight());
    }

    private static javafx.scene.text.FontPosture toFxFontPosture(FontPosture posture) {
        return posture == null ? null : javafx.scene.text.FontPosture.findByName(posture.name());
    }
}
