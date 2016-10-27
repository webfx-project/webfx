package naga.providers.toolkit.swing.drawing.view;

import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.implbase.TextShapeViewImplBase;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTextShapeView extends TextShapeViewImplBase implements SwingShapeView<TextShape> {

    private final SwingShapeBinderPainter swingShapeBinderPainter = new SwingShapeBinderPainter((g) ->
        SwingFonts.toSwingFont(shape.getFont()).createGlyphVector(g.getFontRenderContext(), shape.getText()).getOutline()
    );

    @Override
    public void bind(TextShape shape, DrawingNotifier drawingNotifier) {
        swingShapeBinderPainter.bind(shape);
        super.bind(shape, drawingNotifier);
    }

    @Override
    public void paintShape(Graphics2D g) {
        g.translate(shape.getX(), shape.getY());
        swingShapeBinderPainter.applyCommonShapePropertiesToGraphicsAndPaintShape(shape, g);
    }
}
