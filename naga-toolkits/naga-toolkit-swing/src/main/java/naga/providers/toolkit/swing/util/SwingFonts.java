package naga.providers.toolkit.swing.util;

import naga.toolkit.fx.text.Font;
import naga.toolkit.fx.text.FontPosture;
import naga.toolkit.fx.text.FontWeight;

import static java.awt.Font.*;

/**
 * @author Bruno Salmon
 */
public class SwingFonts {

    public static java.awt.Font toSwingFont(Font font) {
        return font == null ? null : new java.awt.Font(font.getFamily(), toSwingFontStyle(font.getWeight(), font.getPosture()), (int) (font.getSize() + 0.5));
    }

    private static int toSwingFontStyle(FontWeight weight, FontPosture posture) {
        int style = PLAIN;
        if (weight != null && weight.getWeight() >= FontWeight.SEMI_BOLD.getWeight())
            style |= BOLD;
        if (posture == FontPosture.ITALIC)
            style |= ITALIC;
        return style;
    }

}
