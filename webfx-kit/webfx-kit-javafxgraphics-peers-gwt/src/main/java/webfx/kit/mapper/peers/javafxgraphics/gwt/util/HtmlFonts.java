package webfx.kit.mapper.peers.javafxgraphics.gwt.util;

import elemental2.dom.Element;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlNodePeer;

/**
 * @author Bruno Salmon
 */
public final class HtmlFonts {

    public static String getHtmlFontDefinition(Font font) {
        return getHtmlFontStyle(font) + " " + getHtmlFontWeight(font) + " " + getHtmlFontSize(font) + " " + getHtmlFontFamily(font);
    }

    public static void setHtmlFontStyleAttributes(Font font, Element element) {
        if (font != null) {
            HtmlUtil.setStyleAttribute(element,"font-family", getHtmlFontFamily(font));
            HtmlUtil.setStyleAttribute(element,"font-style", getHtmlFontStyle(font));
            HtmlUtil.setStyleAttribute(element,"font-weight", getHtmlFontWeight(font));
            HtmlUtil.setStyleAttribute(element,"font-size", getHtmlFontSize(font));
        }
    }

    public static String getHtmlFontFamily(Font font) {
        return font.getFamily();

    }

    public static String getHtmlFontStyle(Font font) {
        return font.getPosture() == FontPosture.ITALIC ? "italic" : "normal";
    }

    public static double getHtmlFontWeight(Font font) {
        return font.getWeight() == null ? 0d : font.getWeight().getWeight();
    }

    public static String getHtmlFontSize(Font font) {
        return HtmlNodePeer.toPx(font.getSize());
    }
}
