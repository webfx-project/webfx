package naga.providers.toolkit.swing.fx.viewer;

import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.providers.toolkit.swing.fx.SwingScene;
import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.fx.geometry.BoundingBox;
import naga.toolkit.fx.geometry.Bounds;
import naga.toolkit.fx.geometry.VPos;
import naga.toolkit.fx.scene.text.Font;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.scene.text.TextAlignment;
import naga.toolkit.fx.spi.viewer.base.TextViewerBase;
import naga.toolkit.fx.spi.viewer.base.TextViewerMixin;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

/**
 * @author Bruno Salmon
 */
public class SwingTextViewer
        <N extends Text, NB extends TextViewerBase<N, NB, NM>, NM extends TextViewerMixin<N, NB, NM>>
        extends SwingShapeViewer<N, NB, NM>
        implements TextViewerMixin<N, NB, NM> {

    private java.awt.Font swingFont;

    public SwingTextViewer() {
        this((NB) new TextViewerBase());
    }

    SwingTextViewer(NB base) {
        super(base);
    }

    @Override
    public void updateText(String text) {
        invalidateSwingShape();
    }

    @Override
    public void updateFont(Font font) {
        swingFont = null;
        invalidateSwingShape();
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

    private GlyphVector glyphVector;
    @Override
    protected Shape createSwingShape(Graphics2D g) {
        glyphVector = getShapeSwingFont(g).createGlyphVector(g.getFontRenderContext(), getSafeNodeText());
        return glyphVector.getOutline();
    }

    private String getSafeNodeText() {
        return Strings.toSafeString(getNode().getText());
    }

    @Override
    public void prepareCanvasContext(Graphics2D g) {
        super.prepareCanvasContext(g);
        Text t = getNode();
        double x = t.getX();
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
        g.translate(x, t.getY() + textOriginToBaselineOffset(t.getTextOrigin(), g));
    }

    private double textOriginToBaselineOffset(VPos vpos, Graphics2D g) {
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

    @Override
    protected Bounds createLayoutBounds() {
        if (glyphVector == null) {
            SwingScene scene = (SwingScene) getNode().getScene();
            Graphics2D g = (Graphics2D) scene.getSceneComponent().getGraphics();
            g.setFont(getShapeSwingFont(g));
            getOrCreateSwingShape(g);
        }
        Rectangle2D vb = glyphVector.getVisualBounds();
        return new BoundingBox(vb.getMinX(), vb.getMinY(), vb.getWidth(), vb.getHeight());
    }
}
