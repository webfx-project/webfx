package naga.providers.toolkit.swing.drawing.view;


import naga.commons.util.function.Function;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.util.Properties;

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

    void applyCommonShapePropertiesToGraphics(Shape shape, Graphics2D g) {
        boolean smooth = shape.isSmooth();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, smooth ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, smooth ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    void applyCommonShapePropertiesToGraphicsAndPaintShape(Shape shape, Graphics2D g) {
        applyCommonShapePropertiesToGraphics(shape, g);
        paintShape(g);
    }

    void paintShape(Graphics2D g) {
        paintSwingShape(createSwingShape(g), g);
    }

    void paintShape(Double width, Double height, Graphics2D g) {
        paintSwingShape(createSwingShape(g), width, height, g);
    }

    private java.awt.Shape createSwingShape(Graphics2D g) {
        return swingShapeFactory.apply(g);
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
