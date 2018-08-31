package webfx.fx.spi.gwt.svg.peer;

import webfx.util.Numbers;
import webfx.fx.spi.gwt.util.SvgUtil;
import emul.javafx.geometry.VPos;
import emul.javafx.scene.text.Font;
import emul.javafx.scene.text.Text;
import emul.javafx.scene.text.TextAlignment;
import webfx.fx.spi.peer.base.TextPeerBase;
import webfx.fx.spi.peer.base.TextPeerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgTextPeer
        <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>
        extends SvgShapePeer<N, NB, NM>
        implements TextPeerMixin<N, NB, NM> {

    public SvgTextPeer() {
        this((NB) new TextPeerBase());
    }

    public SvgTextPeer(NB base) {
        super(base, SvgUtil.createSvgText());
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

    @Override
    public void updateY(Double y) {
        setElementAttribute("y", y);
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        setElementAttribute("width", wrappingWidth);
        updateX(getNode().getX());
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        setElementAttribute("text-anchor", textAlignmentToSvgTextAnchor(textAlignment));
    }

    @Override
    public void updateFont(Font font) {
        setFontAttributes(font);
    }
}
