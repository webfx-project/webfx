package naga.providers.toolkit.swing.drawing.view;

import naga.commons.util.Numbers;
import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.drawing.shapes.*;
import naga.toolkit.drawing.spi.view.base.TextShapeViewBase;
import naga.toolkit.drawing.spi.view.base.TextShapeViewMixin;

import java.awt.*;
import java.awt.Font;
import java.awt.Shape;

/**
 * @author Bruno Salmon
 */
public class SwingTextShapeView
        extends SwingShapeView<TextShape, TextShapeViewBase, TextShapeViewMixin>
        implements TextShapeViewMixin {

    public SwingTextShapeView() {
        super(new TextShapeViewBase());
    }

    @Override
    public void updateText(String text) {

    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {

    }

    @Override
    public void updateX(Double x) {

    }

    @Override
    public void updateY(Double y) {

    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {

    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {

    }

    @Override
    public void updateFont(naga.toolkit.drawing.shapes.Font font) {

    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        return getShapeSwingFont().createGlyphVector(g.getFontRenderContext(), getDrawable().getText()).getOutline();
    }

    private Font getShapeSwingFont() {
        return SwingFonts.toSwingFont(getDrawable().getFont());
    }

    @Override
    public void paint(Graphics2D g) {
        TextShape ts = getDrawable();
        double x = Numbers.doubleValue(ts.getX());
        double wrappingWidth = Numbers.doubleValue(ts.getWrappingWidth());
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            int textWidth = g.getFontMetrics(getShapeSwingFont()).stringWidth(ts.getText());
            TextAlignment textAlignment = ts.getTextAlignment();
            if (textAlignment == TextAlignment.CENTER)
                x += (wrappingWidth - textWidth) / 2;
            else if (textAlignment == TextAlignment.RIGHT)
                x += (wrappingWidth - textWidth);
        }
        g.translate(x, ts.getY() + vPosToBaselineOffset(ts.getTextOrigin(), g));
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
