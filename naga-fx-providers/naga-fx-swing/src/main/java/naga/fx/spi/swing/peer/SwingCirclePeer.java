package naga.fx.spi.swing.peer;

import emul.javafx.scene.shape.Circle;
import naga.fx.spi.peer.base.CirclePeerMixin;
import naga.fx.spi.peer.base.CirclePeerBase;

import java.awt.*;
import java.awt.geom.Ellipse2D;

/**
 * @author Bruno Salmon
 */
public class SwingCirclePeer
        <N extends Circle, NB extends CirclePeerBase<N, NB, NM>, NM extends CirclePeerMixin<N, NB, NM>>

        extends SwingShapePeer<N, NB, NM>
        implements CirclePeerMixin<N, NB, NM> {

    public SwingCirclePeer() {
        this((NB) new CirclePeerBase());
    }

    public SwingCirclePeer(NB base) {
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
