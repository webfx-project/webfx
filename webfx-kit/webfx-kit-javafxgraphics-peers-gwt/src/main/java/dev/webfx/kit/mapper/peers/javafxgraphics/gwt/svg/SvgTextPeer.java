package dev.webfx.kit.mapper.peers.javafxgraphics.gwt.svg;

import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.base.TextPeerMixin;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.util.SvgUtil;
import dev.webfx.platform.util.Numbers;
import javafx.geometry.VPos;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public final class SvgTextPeer
        <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>
        extends SvgShapePeer<N, NB, NM>
        implements TextPeerMixin<N, NB, NM> {

    public SvgTextPeer() {
        this((NB) new TextPeerBase());
    }

    public SvgTextPeer(NB base) {
        super(base, SvgUtil.createSvgText());
        // Removing outline (by setting with=0) which otherwise appears when clicking on the text (default user agent behavior)
        setElementStyleAttribute("outline-width", "0");
    }

    @Override
    public void updateText(String text) {
        setElementTextContent(text);
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        setElementAttribute("dominant-baseline", vPosToSvgAlignmentBaseLine(textOrigin));
    }

    @Override
    public void updateX(Double X) {
        Text t = getNodePeerBase().getNode();
        double x = Numbers.doubleValue(X);
        double wrappingWidth = Numbers.doubleValue(t.getWrappingWidth());
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            TextAlignment textAlignment = t.getTextAlignment();
            if (textAlignment == TextAlignment.CENTER)
                x += wrappingWidth / 2;
            else if (textAlignment == TextAlignment.RIGHT)
                x += wrappingWidth;
        }
        setElementAttribute("x", x);
    }

    private void updateX() {
        updateX(getNode().getX());
    }

    @Override
    public void updateY(Double y) {
        // In Svg, text.y refers to the bottom, whereas in JavaFX, it refers to the top
        setElementAttribute("y", y + getBBox().height); // So we consider this difference here
    }

    private void updateY() {
        updateY(getNode().getY());
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        setElementAttribute("width", wrappingWidth);
        updateX();
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        setElementAttribute("text-anchor", textAlignmentToSvgTextAnchor(textAlignment));
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
        updateY();
    }
}
