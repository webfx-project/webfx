package naga.providers.toolkit.html.fx.svg.view;

import naga.commons.util.Numbers;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.spi.viewer.base.TextViewerBase;
import naga.toolkit.fx.spi.viewer.base.TextViewerMixin;

/**
 * @author Bruno Salmon
 */
public class SvgTextViewer
        <N extends Text, NB extends TextViewerBase<N, NB, NM>, NM extends TextViewerMixin<N, NB, NM>>
        extends SvgShapeViewer<N, NB, NM>
        implements TextViewerMixin<N, NB, NM> {

    public SvgTextViewer() {
        this((NB) new TextViewerBase());
    }

    public SvgTextViewer(NB base) {
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
        Text t = getNodeViewerBase().getNode();
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
