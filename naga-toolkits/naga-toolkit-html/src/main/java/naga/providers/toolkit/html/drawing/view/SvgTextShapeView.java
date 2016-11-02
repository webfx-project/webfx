package naga.providers.toolkit.html.drawing.view;

import naga.commons.util.Numbers;
import naga.providers.toolkit.html.util.SvgUtil;
import naga.toolkit.drawing.shapes.Font;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.shapes.VPos;
import naga.toolkit.drawing.spi.view.base.TextShapeViewBase;
import naga.toolkit.drawing.spi.view.base.TextShapeViewMixin;

/**
 * @author Bruno Salmon
 */
public class SvgTextShapeView
        extends SvgShapeView<TextShape, TextShapeViewBase, TextShapeViewMixin>
        implements TextShapeViewMixin {

    public SvgTextShapeView() {
        super(new TextShapeViewBase(), SvgUtil.createSvgText());
    }

    @Override
    public void updateText(String text) {
        setSvgTextContent(text);
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        setSvgAttribute("dominant-baseline", vPosToSvgAlignmentBaseLine(textOrigin));
    }

    @Override
    public void updateX(Double X) {
        TextShape ts = getDrawableViewBase().getDrawable();
        double x = Numbers.doubleValue(X);
        double wrappingWidth = Numbers.doubleValue(ts.getWrappingWidth());
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            TextAlignment textAlignment = ts.getTextAlignment();
            if (textAlignment == TextAlignment.CENTER)
                x = x + wrappingWidth / 2;
            else if (textAlignment == TextAlignment.RIGHT)
                x = x + wrappingWidth;
        }
        setSvgAttribute("x", x);
    }

    @Override
    public void updateY(Double y) {
        setSvgAttribute("y", y);
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        setSvgAttribute("width", wrappingWidth);
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        setSvgAttribute("text-anchor", textAlignmentToSvgTextAnchor(textAlignment));
    }

    @Override
    public void updateFont(Font font) {
        setSvgFontAttributes(font);
    }
}
