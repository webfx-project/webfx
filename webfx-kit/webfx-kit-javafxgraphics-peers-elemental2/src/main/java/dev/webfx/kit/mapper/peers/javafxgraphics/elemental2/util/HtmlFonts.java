package dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.util;

import elemental2.dom.Element;
import javafx.scene.text.Font;
import javafx.scene.text.FontPosture;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlNodePeer;

/**
 * @author Bruno Salmon
 */
public final class HtmlFonts {

    public static String getHtmlFontDefinition(Font font) {
        if (font == null)
            return null;
        String htmlFontStyle = getHtmlFontStyle(font);
        Integer htmlFontWeight = getHtmlFontWeight(font);
        String htmlFontSize = getHtmlFontSize(font);
        String htmlFontFamily = getHtmlFontFamily(font);
        StringBuilder sb = new StringBuilder();
        if (htmlFontStyle != null)
            sb.append(htmlFontStyle).append(" ");
        if (htmlFontWeight != null)
            sb.append(htmlFontWeight).append(" ");
        if (htmlFontSize != null)
            sb.append(htmlFontSize).append(" ");
        if (htmlFontFamily != null)
            sb.append(htmlFontFamily);
        return sb.toString();
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
        FontPosture posture = font.getPosture();
        return posture == null ? null : // ignoring posture if not set
            posture == FontPosture.ITALIC ? "italic" : "normal";
    }

    public static Integer getHtmlFontWeight(Font font) {
        return font.getWeight() == null ? null : // Ignoring weight if not set
            font.getWeight().getWeight();
    }

    public static String getHtmlFontSize(Font font) {
        double size = font.getSize();
        return size < 0 ? null : // ignoring size if not set (i.e., negative - typically -1)
            HtmlNodePeer.toPx(size);
    }
}
