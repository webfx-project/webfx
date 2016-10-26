package naga.providers.toolkit.swing.drawing.view;


import naga.commons.util.function.Function;
import naga.toolkit.drawing.shapes.Shape;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Bruno Salmon
 */
class SwingShapeBinderPainter {

    private final SwingPaintUpdater swingPaintUpdater = new SwingPaintUpdater();
    private final SwingStrokeUpdater swingStrokeUpdater = new SwingStrokeUpdater();
    private final Function<Graphics2D, java.awt.Shape> swingShapeFactory;

    public SwingShapeBinderPainter(Function<Graphics2D, java.awt.Shape> swingShapeFactory) {
        this.swingShapeFactory = swingShapeFactory;
    }

    void bind(Shape shape) {
        shape.fillProperty().addListener((observable, oldValue, newValue) -> swingPaintUpdater.updateFromShape(shape));
        swingPaintUpdater.updateFromShape(shape);
        shape.strokeProperty().addListener((observable, oldValue, newValue) -> swingStrokeUpdater.updateFromShape(shape));
        shape.strokeWidthProperty().addListener((observable, oldValue, newValue) -> swingStrokeUpdater.updateFromShape(shape));
        swingStrokeUpdater.updateFromShape(shape);
    }

    void paintShape(Graphics2D g) {
        paintShape(swingShapeFactory.apply(g), g);
    }

    void paintShape(Double width, Double height, Graphics2D g) {
        paintShape(swingShapeFactory.apply(g), width, height, g);
    }

    void paintShape(java.awt.Shape swingShape, Graphics2D g) {
        Rectangle2D bounds2D = swingShape.getBounds2D();
        paintShape(swingShape, bounds2D.getWidth(), bounds2D.getHeight(), g);
    }

    void paintShape(java.awt.Shape swingShape, Double width, Double height, Graphics2D g) {
        swingPaintUpdater.updateProportionalGradient(width, height);
        g.setPaint(swingPaintUpdater.swingPaint);
        g.fill(swingShape);
        if (swingStrokeUpdater.swingStroke != null) {
            g.setStroke(swingStrokeUpdater.swingStroke);
            swingStrokeUpdater.updateProportionalGradient(width, height);
            g.setPaint(swingStrokeUpdater.swingPaint);
            g.draw(swingShape);
        }
    }
}
