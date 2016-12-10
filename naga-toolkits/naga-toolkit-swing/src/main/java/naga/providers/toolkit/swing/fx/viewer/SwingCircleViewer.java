package naga.providers.toolkit.swing.fx.viewer;

import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.spi.viewer.base.CircleViewerBase;
import naga.toolkit.fx.spi.viewer.base.CircleViewerMixin;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Bruno Salmon
 */
public class SwingCircleViewer
        extends SwingShapeViewer<Circle, CircleViewerBase, CircleViewerMixin>
        implements CircleViewerMixin {

    public SwingCircleViewer() {
        super(new CircleViewerBase());
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
