package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.DrawingNotifier;
import naga.toolkit.drawing.spi.view.implbase.RectangleViewImplBase;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Bruno Salmon
 */
public class SwingRectangleView extends RectangleViewImplBase implements SwingDrawableView<Rectangle> {

    private final SwingShapeBinderPainter swingShapeBinderPainter = new SwingShapeBinderPainter((g) -> {
        Double arcWidth = drawable.getArcWidth();
        Double arcHeight = drawable.getArcHeight();
        if (arcWidth != null && arcHeight != null && arcWidth != 0 && arcHeight != 0)
            return new RoundRectangle2D.Double(drawable.getX(), drawable.getY(), drawable.getWidth(), drawable.getHeight(), arcWidth, arcHeight);
        return new Rectangle2D.Double(drawable.getX(), drawable.getY(), drawable.getWidth(), drawable.getHeight());
    });

    @Override
    public void bind(Rectangle drawable, DrawingNotifier drawingNotifier) {
        super.bind(drawable, drawingNotifier);
        swingShapeBinderPainter.bind(drawable);
    }

    @Override
    public void paintShape(Graphics2D g) {
        swingShapeBinderPainter.applyCommonShapePropertiesToGraphics(drawable, g);
        swingShapeBinderPainter.paintShape(drawable.getWidth(), drawable.getHeight(), g);
    }
}
