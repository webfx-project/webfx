package naga.providers.toolkit.swing.drawing.view;

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

    private final Font font = new Font("Serif", Font.PLAIN, 100);

    private final SwingShapeBinderPainter swingShapeBinderPainter = new SwingShapeBinderPainter((g) -> {
        String text = shape.getText();
        FontRenderContext fontRenderContext = g.getFontRenderContext();
        g.translate(shape.getX(), shape.getY() /*+ font.getLineMetrics(text, fontRenderContext).getAscent()*/);
        GlyphVector gv = font.createGlyphVector(fontRenderContext, text);
        return gv.getOutline();
    });

    @Override
    public void bind(TextShape shape, DrawingNotifier drawingNotifier) {
        super.bind(shape, drawingNotifier);
        swingShapeBinderPainter.bind(shape);
    }

    @Override
    public void paintShape(Graphics2D g) {
        swingShapeBinderPainter.paintShape(g);
    }
}
