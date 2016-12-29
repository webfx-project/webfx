package naga.fx.spi.swing.fx.viewer;

import naga.fx.scene.shape.Circle;
import naga.fx.spi.viewer.base.CircleViewerBase;
import naga.fx.spi.viewer.base.CircleViewerMixin;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Bruno Salmon
 */
public class SwingCircleViewer
        <N extends Circle, NB extends CircleViewerBase<N, NB, NM>, NM extends CircleViewerMixin<N, NB, NM>>

        extends SwingShapeViewer<N, NB, NM>
        implements CircleViewerMixin<N, NB, NM> {

    public SwingCircleViewer() {
        this((NB) new CircleViewerBase());
    }

    public SwingCircleViewer(NB base) {
        super(base);
    }

    @Override
    public void updateCenterX(Double centerX) {
        invalidateSwingShape();
    }

    @Override
    public void updateCenterY(Double centerY) {
        invalidateSwingShape();
    }

    @Override
    public void updateRadius(Double radius) {
        invalidateSwingShape();
    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        Circle c = getNode();
        Double radius = c.getRadius();
        return new Ellipse2D.Double(c.getCenterX() - radius, c.getCenterY() - radius, 2 * radius, 2 * radius);
    }
}
