package naga.providers.toolkit.swing.drawing.view;

import naga.commons.util.Numbers;
import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.shapes.VPos;
import naga.toolkit.drawing.spi.view.base.TextShapeViewBase;
import naga.toolkit.drawing.spi.view.mixin.TextShapeViewMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTextShapeView extends SwingShapeView<TextShape> implements TextShapeViewMixin {

    private final TextShapeViewBase base = new TextShapeViewBase();
    @Override
    public TextShapeViewBase getDrawableViewBase() {
        return base;
    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        return getShapeSwingFont().createGlyphVector(g.getFontRenderContext(), drawable.getText()).getOutline();
    }

    private Font getShapeSwingFont() {
        return SwingFonts.toSwingFont(drawable.getFont());
    }

    @Override
    public void paint(Graphics2D g) {
        double x = Numbers.doubleValue(drawable.getX());
        double wrappingWidth = Numbers.doubleValue(drawable.getWrappingWidth());
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            int textWidth = g.getFontMetrics(getShapeSwingFont()).stringWidth(drawable.getText());
            TextAlignment textAlignment = drawable.getTextAlignment();
            if (textAlignment == TextAlignment.CENTER)
                x += (wrappingWidth - textWidth) / 2;
            else if (textAlignment == TextAlignment.RIGHT)
                x += (wrappingWidth - textWidth);
        }
        g.translate(x, drawable.getY() + vPosToBaselineOffset(drawable.getTextOrigin(), g));
        prepareGraphicsAndPaintShape(g);
    }

    private double vPosToBaselineOffset(VPos vpos, Graphics2D g) {
        if (vpos != null && vpos != VPos.BASELINE) {
            FontMetrics fontMetrics = g.getFontMetrics(getShapeSwingFont());
            switch (vpos) {
                case TOP:
                    return fontMetrics.getAscent();
                case CENTER:
                    return fontMetrics.getAscent() - fontMetrics.getHeight() / 2;
                case BOTTOM:
                    return -fontMetrics.getDescent();
            }
        }
        return 0;
    }
}
