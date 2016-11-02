package naga.providers.toolkit.swing.drawing.view;

import javafx.beans.property.Property;
import naga.toolkit.drawing.shapes.Shape;
import naga.toolkit.drawing.spi.view.base.ShapeViewBase;

import java.awt.*;
import java.awt.geom.Rectangle2D;

/**
 * @author Bruno Salmon
 */
abstract class SwingShapeView<S extends Shape> extends ShapeViewBase<S> implements SwingDrawableView<S> {

    private final SwingPaintUpdater swingPaintUpdater = new SwingPaintUpdater();
    private final SwingStrokeUpdater swingStrokeUpdater = new SwingStrokeUpdater();
    private java.awt.Shape swingShape;

    @Override
    public void update(Property changedProperty) {
        updateSwingShape(changedProperty);
    }

    @Override
    public void paint(Graphics2D g) {
        prepareGraphicsAndPaintShape(g);
    }

    void updateSwingShape(Property changedProperty) {
        if (swingPaintUpdater.updateFromShape(drawable, changedProperty))
            return;
        if (swingStrokeUpdater.updateFromShape(drawable, changedProperty))
            return;
        swingShape = null;
    }

    private java.awt.Shape getOrCreateSwingShape(Graphics2D g) {
        if (swingShape == null)
            swingShape =  createSwingShape(g);
        return swingShape;
    }

    protected abstract java.awt.Shape createSwingShape(Graphics2D g);

    void prepareGraphics(Graphics2D g) {
        boolean smooth = drawable.isSmooth();
        g.setRenderingHint(RenderingHints.KEY_ANTIALIASING, smooth ? RenderingHints.VALUE_ANTIALIAS_ON : RenderingHints.VALUE_ANTIALIAS_OFF);
        g.setRenderingHint(RenderingHints.KEY_TEXT_ANTIALIASING, smooth ? RenderingHints.VALUE_TEXT_ANTIALIAS_ON : RenderingHints.VALUE_TEXT_ANTIALIAS_OFF);
    }

    void prepareGraphicsAndPaintShape(Graphics2D g) {
        prepareGraphics(g);
        paintSwingShape(g);
    }

    void paintSwingShape(Graphics2D g) {
        getOrCreateSwingShape(g);
        Rectangle2D bounds2D = swingShape.getBounds2D();
        paintSwingShape(bounds2D.getWidth(), bounds2D.getHeight(), g);
    }

    void paintSwingShape(Double width, Double height, Graphics2D g) {
        getOrCreateSwingShape(g);
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
