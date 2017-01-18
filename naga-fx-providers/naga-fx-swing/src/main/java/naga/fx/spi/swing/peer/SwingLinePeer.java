package naga.fx.spi.swing.peer;

import emul.javafx.scene.shape.Line;
import naga.fx.spi.peer.base.LinePeerBase;
import naga.fx.spi.peer.base.LinePeerMixin;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * @author Bruno Salmon
 */
public class SwingLinePeer
        <N extends Line, NB extends LinePeerBase<N, NB, NM>, NM extends LinePeerMixin<N, NB, NM>>

        extends SwingShapePeer<N, NB, NM>
        implements LinePeerMixin<N, NB, NM> {

    public SwingLinePeer() {
        this((NB) new LinePeerBase());
    }

    public SwingLinePeer(NB base) {
        super(base);
    }

    @Override
    public void updateStartX(Double startX) {
        invalidateSwingShape();
    }

    @Override
    public void updateStartY(Double startY) {
        invalidateSwingShape();
    }

    @Override
    public void updateEndX(Double endX) {
        invalidateSwingShape();
    }

    @Override
    public void updateEndY(Double endY) {
        invalidateSwingShape();
    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        Line l = getNode();
        return new Line2D.Double(l.getStartX(), l.getStartY(), l.getEndX(), l.getEndY());
    }
}
