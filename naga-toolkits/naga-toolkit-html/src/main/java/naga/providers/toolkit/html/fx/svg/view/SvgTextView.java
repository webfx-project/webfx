package naga.providers.toolkit.html.fx.svg.view;

import naga.commons.util.Numbers;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.spi.view.base.TextViewBase;
import naga.toolkit.fx.spi.view.base.TextViewMixin;
import naga.toolkit.fx.text.Font;
import naga.toolkit.fx.text.Text;
import naga.toolkit.fx.text.TextAlignment;

/**
 * @author Bruno Salmon
 */
public class SvgTextView
        extends SvgShapeView<Text, TextViewBase, TextViewMixin>
        implements TextViewMixin {

    public SvgTextView() {
        super(new TextViewBase(), SvgUtil.createSvgText());
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
        Text t = getNodeViewBase().getNode();
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
