package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shape.Circle;
import naga.toolkit.drawing.spi.view.base.CircleViewBase;
import naga.toolkit.drawing.spi.view.base.CircleViewMixin;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Bruno Salmon
 */
public class SwingCircleView
        extends SwingShapeView<Circle, CircleViewBase, CircleViewMixin>
        implements CircleViewMixin {

    public SwingCircleView() {
        super(new CircleViewBase());
    }

    @Override
    public void updateCenterX(Double centerX) {
        updateSwingShape();
    }

    @Override
    public void updateCenterY(Double centerY) {
        updateSwingShape();
    }

    @Override
    public void updateRadius(Double radius) {
        updateSwingShape();
    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        Circle c = getNode();
        Double radius = c.getRadius();
        return new Ellipse2D.Double(c.getCenterX() - radius, c.getCenterY() - radius, 2 * radius, 2 * radius);
    }
}
