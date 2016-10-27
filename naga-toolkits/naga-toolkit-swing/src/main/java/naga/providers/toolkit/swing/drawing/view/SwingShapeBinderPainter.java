package naga.providers.toolkit.swing.drawing.view;


import naga.commons.util.function.Function;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.properties.util.Properties;

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
        Properties.runNowAndOnPropertiesChange(() -> swingPaintUpdater.updateFromShape(shape), shape.fillProperty());
        Properties.runNowAndOnPropertiesChange(() -> swingStrokeUpdater.updateFromShape(shape), shape.strokeProperty(), shape.strokeWidthProperty(), shape.strokeLineCapProperty(), shape.strokeLineJoinProperty(), shape.strokeMiterLimitProperty(), shape.strokeDashOffsetProperty());
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
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
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
