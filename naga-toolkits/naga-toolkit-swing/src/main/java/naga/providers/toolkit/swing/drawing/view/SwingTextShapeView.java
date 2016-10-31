package naga.providers.toolkit.swing.drawing.view;

import naga.commons.util.Numbers;
import naga.providers.toolkit.swing.util.SwingFonts;
import naga.toolkit.drawing.shapes.TextAlignment;
import naga.toolkit.drawing.shapes.TextShape;
import naga.toolkit.drawing.shapes.VPos;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.implbase.TextShapeViewImplBase;

import java.awt.*;

/**
 * @author Bruno Salmon
 */
public class SwingTextShapeView extends TextShapeViewImplBase implements SwingShapeView<TextShape> {

    private final SwingShapeBinderPainter swingShapeBinderPainter = new SwingShapeBinderPainter((g) ->
        getShapeSwingFont().createGlyphVector(g.getFontRenderContext(), shape.getText()).getOutline()
    );

    private Font getShapeSwingFont() {
        return SwingFonts.toSwingFont(shape.getFont());
    }

    @Override
    public void bind(TextShape shape, DrawingNotifier drawingNotifier) {
        swingShapeBinderPainter.bind(shape);
        super.bind(shape, drawingNotifier);
    }

    @Override
    public void paintShape(Graphics2D g) {
        double x = Numbers.doubleValue(shape.getX());
        double wrappingWidth = Numbers.doubleValue(shape.getWrappingWidth());
        // Partial implementation that doesn't support multi-line text wrapping. TODO: Add multi-line wrapping support
        if (wrappingWidth > 0) {
            int textWidth = g.getFontMetrics(getShapeSwingFont()).stringWidth(shape.getText());
            TextAlignment textAlignment = shape.getTextAlignment();
            if (textAlignment == TextAlignment.CENTER)
                x += (wrappingWidth - textWidth) / 2;
            else if (textAlignment == TextAlignment.RIGHT)
                x += (wrappingWidth - textWidth);
        }
        g.translate(x, shape.getY() + vPosToBaselineOffset(shape.getTextOrigin(), g));
        swingShapeBinderPainter.applyCommonShapePropertiesToGraphicsAndPaintShape(shape, g);
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
