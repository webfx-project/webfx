package naga.providers.toolkit.swing.fx.view;

import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.spi.view.base.TextViewBase;
import naga.toolkit.fx.spi.view.base.TextViewMixin;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTextView
        extends SwingShapeView<Text, TextViewBase, TextViewMixin>
        implements TextViewMixin {

    private java.awt.Font swingFont;

    public SwingTextView() {
        super(new TextViewBase());
    }

    @Override
    public void updateText(String text) {
        updateSwingShape();
    }

    @Override
    public void updateFont(Font font) {
        swingFont = null;
        updateSwingShape();
    }

    @Override
    public void updateTextOrigin(VPos textOrigin) {
        // Doesn't affect the shape, textOrigin will be used in prepareCanvasContext() translation
    }

    @Override
    public void updateX(Double x) {
        // Doesn't affect the shape, x will be used in prepareCanvasContext() translation
    }

    @Override
    public void updateY(Double y) {
        // Doesn't affect the shape, y will be used in prepareCanvasContext() translation
    }

    @Override
    public void updateWrappingWidth(Double wrappingWidth) {
        // Doesn't affect the shape, wrappingWidth will be used in prepareCanvasContext() translation
    }

    @Override
    public void updateTextAlignment(TextAlignment textAlignment) {
        // Doesn't affect the shape, textAlignment will be used in prepareCanvasContext() translation
    }

    private java.awt.Font getShapeSwingFont() {
        if (swingFont == null)
            swingFont = SwingFonts.toSwingFont(getNode().getFont());
        return swingFont;
    }

    private java.awt.Font getShapeSwingFont(Graphics2D g) {
        java.awt.Font font = getShapeSwingFont();
        return font != null ? font : g.getFont();
    }

    private FontMetrics getFontMetrics(Graphics2D g) {
        return g.getFontMetrics(getShapeSwingFont(g));
    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        return getShapeSwingFont(g).createGlyphVector(g.getFontRenderContext(), getSafeNodeText()).getOutline();
    }

    private String getSafeNodeText() {
        return Strings.toSafeString(getNode().getText());
    }

    @Override
    public void prepareCanvasContext(Graphics2D g) {
        super.prepareCanvasContext(g);
        Text t = getNode();
        double x = Numbers.doubleValue(t.getX());
        double wrappingWidth = Numbers.doubleValue(t.getWrappingWidth());
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            int textWidth = getFontMetrics(g).stringWidth(getSafeNodeText());
            TextAlignment textAlignment = t.getTextAlignment();
            if (textAlignment == TextAlignment.CENTER)
                x += (wrappingWidth - textWidth) / 2;
            else if (textAlignment == TextAlignment.RIGHT)
                x += (wrappingWidth - textWidth);
        }
        g.translate(x, t.getY() + vPosToBaselineOffset(t.getTextOrigin(), g));
    }

    private double vPosToBaselineOffset(VPos vpos, Graphics2D g) {
        if (vpos != null && vpos != VPos.BASELINE) {
            FontMetrics fontMetrics = getFontMetrics(g);
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
