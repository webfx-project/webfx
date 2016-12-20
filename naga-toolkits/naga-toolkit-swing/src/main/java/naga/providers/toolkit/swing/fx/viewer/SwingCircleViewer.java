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
        <N extends Circle, NV extends CircleViewerBase<N, NV, NM>, NM extends CircleViewerMixin<N, NV, NM>>

        extends SwingShapeViewer<N, NV, NM>
        implements CircleViewerMixin<N, NV, NM> {

    public SwingCircleViewer() {
        this((NV) new CircleViewerBase());
    }

    public SwingCircleViewer(NV base) {
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
