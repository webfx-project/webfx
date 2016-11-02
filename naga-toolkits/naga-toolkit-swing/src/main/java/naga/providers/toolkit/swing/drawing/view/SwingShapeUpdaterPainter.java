package naga.providers.toolkit.swing.drawing.view;


import javafx.beans.property.Property;
import naga.commons.util.function.Function;
import naga.toolkit.drawing.shapes.Shape;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Bruno Salmon
 */
class SwingShapeUpdaterPainter {

    private final SwingPaintUpdater swingPaintUpdater = new SwingPaintUpdater();
    private final SwingStrokeUpdater swingStrokeUpdater = new SwingStrokeUpdater();
    private final Function<Graphics2D, java.awt.Shape> swingShapeFactory;
    private java.awt.Shape swingShape;

    SwingShapeUpdaterPainter(Function<Graphics2D, java.awt.Shape> swingShapeFactory) {
        this.swingShapeFactory = swingShapeFactory;
    }

    void updateSwingShape(Shape shape, Property changedProperty) {
        if (swingPaintUpdater.updateFromShape(shape, changedProperty))
            return;
        if (swingStrokeUpdater.updateFromShape(shape, changedProperty))
            return;
        swingShape = null;
    }

    private java.awt.Shape getOrCreateSwingShape(Graphics2D g) {
        if (swingShape == null)
            swingShape =  swingShapeFactory.apply(g);
        return swingShape;
    }

    void prepareGraphics(Shape shape, Graphics2D g) {
        boolean smooth = shape.isSmooth();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, smooth ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, smooth ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    void prepareGraphicsAndPaintShape(Shape shape, Graphics2D g) {
        prepareGraphics(shape, g);
        paintSwingShape(g);
    }

    void paintSwingShape(Graphics2D g) {
        paintSwingShape(getOrCreateSwingShape(g), g);
    }

    void paintSwingShape(Double width, Double height, Graphics2D g) {
        paintSwingShape(getOrCreateSwingShape(g), width, height, g);
    }

    void paintSwingShape(java.awt.Shape swingShape, Graphics2D g) {
        Rectangle2D bounds2D = swingShape.getBounds2D();
        paintSwingShape(swingShape, bounds2D.getWidth(), bounds2D.getHeight(), g);
    }

    void paintSwingShape(java.awt.Shape swingShape, Double width, Double height, Graphics2D g) {
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
