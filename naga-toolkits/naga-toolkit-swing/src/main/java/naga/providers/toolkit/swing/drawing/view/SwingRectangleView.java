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
public class SwingRectangleView extends RectangleViewImplBase implements SwingShapeView<Rectangle> {

    private final SwingShapeBinderPainter swingShapeBinderPainter = new SwingShapeBinderPainter((g) -> {
        Double arcWidth = shape.getArcWidth();
        Double arcHeight = shape.getArcHeight();
        if (arcWidth != null && arcHeight != null && arcWidth != 0 && arcHeight != 0)
            return new RoundRectangle2D.Double(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight(), arcWidth, arcHeight);
        return new Rectangle2D.Double(shape.getX(), shape.getY(), shape.getWidth(), shape.getHeight());
    });

    @Override
    public void bind(Rectangle shape, DrawingNotifier drawingNotifier) {
        super.bind(shape, drawingNotifier);
        swingShapeBinderPainter.bind(shape);
    }

    @Override
    public void paintShape(Graphics2D g) {
        swingShapeBinderPainter.applyCommonShapePropertiesToGraphics(shape, g);
        swingShapeBinderPainter.paintShape(shape.getWidth(), shape.getHeight(), g);
    }
}
