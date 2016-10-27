package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.implbase.TextShapeViewImplBase;

import java.awt.*;
import java.awt.font.FontRenderContext;
import java.awt.font.GlyphVector;

/**
 * @author Bruno Salmon
 */
public class SwingTextShapeView extends TextShapeViewImplBase implements SwingShapeView<TextShape> {

    private final SwingShapeBinderPainter swingShapeBinderPainter = new SwingShapeBinderPainter((g) -> {
        String text = shape.getText();
        FontRenderContext fontRenderContext = g.getFontRenderContext();
        Font font = SwingFonts.toSwingFont(shape.getFont());
        g.translate(shape.getX(), shape.getY() /*+ font.getLineMetrics(text, fontRenderContext).getAscent()*/);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, RenderingHints.VALUE_TEXT_ANTIALIAS_ON);
        GlyphVector gv = font.createGlyphVector(fontRenderContext, text);
        return gv.getOutline();
    });

    @Override
    public void bind(TextShape shape, DrawingNotifier drawingNotifier) {
        swingShapeBinderPainter.bind(shape);
        super.bind(shape, drawingNotifier);
    }

    @Override
    public void paintShape(Graphics2D g) {
        swingShapeBinderPainter.paintShape(g);
    }
}
