package naga.fx.spi.swing.peer;

import naga.commons.util.Numbers;
import naga.commons.util.Strings;
import naga.fx.spi.peer.base.TextPeerBase;
import naga.fx.spi.swing.util.SwingFonts;
import naga.fx.geometry.BoundingBox;
import naga.fx.geometry.Bounds;
import naga.fx.geometry.VPos;
import naga.fx.scene.text.Font;
import naga.fx.scene.text.Text;
import naga.fx.scene.text.TextAlignment;
import naga.fx.spi.peer.base.TextPeerMixin;

import java.awt.*;
import java.awt.font.GlyphVector;
import java.awt.geom.Rectangle2D;

/**
 * @author Bruno Salmon
 */
public class SwingTextPeer
        <N extends Text, NB extends TextPeerBase<N, NB, NM>, NM extends TextPeerMixin<N, NB, NM>>
        extends SwingShapePeer<N, NB, NM>
        implements TextPeerMixin<N, NB, NM> {

    private java.awt.Font swingFont;

    public SwingTextPeer() {
        this((NB) new TextPeerBase());
    }

    SwingTextPeer(NB base) {
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
            SwingScenePeer scene = (SwingScenePeer) getNode().getScene().impl_getPeer();
            Graphics2D g = (Graphics2D) scene.getSceneComponent().getGraphics();
            g.setFont(getShapeSwingFont(g));
            getOrCreateSwingShape(g);
        }
        Rectangle2D vb = glyphVector.getVisualBounds();
        return new BoundingBox(vb.getMinX(), vb.getMinY(), vb.getWidth(), vb.getHeight());
    }
}
