package naga.providers.toolkit.swing.fx.view;

import naga.commons.util.Numbers;
import naga.commons.util.Objects;
import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.spi.view.base.TextViewBase;
import naga.toolkit.fx.spi.view.base.TextViewMixin;
import naga.toolkit.fx.spi.view.base.TextViewMixin2;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTextView
        extends SwingShapeView<Text, TextViewBase, TextViewMixin>
        implements TextViewMixin2 {

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

    private java.awt.Font getShapeSwingFont() {
        if (swingFont == null)
            swingFont = SwingFonts.toSwingFont(getNode().getFont());
        return swingFont;
    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        return getShapeSwingFont().createGlyphVector(g.getFontRenderContext(), Objects.coalesce(getNode().getText(), "")).getOutline();
    }

    @Override
    public void prepareCanvasContext(Graphics2D g) {
        super.prepareCanvasContext(g);
        Text t = getNode();
        double x = Numbers.doubleValue(t.getX());
        double wrappingWidth = Numbers.doubleValue(t.getWrappingWidth());
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            int textWidth = g.getFontMetrics(getShapeSwingFont()).stringWidth(Objects.coalesce(t.getText(), ""));
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
