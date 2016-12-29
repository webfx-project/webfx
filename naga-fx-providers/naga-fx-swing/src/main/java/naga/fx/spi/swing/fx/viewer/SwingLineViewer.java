package naga.fx.spi.swing.fx.viewer;

import naga.fx.scene.shape.Line;
import naga.fx.spi.viewer.base.LineViewerBase;
import naga.fx.spi.viewer.base.LineViewerMixin;

import java.awt.*;
import java.awt.geom.Line2D;

/**
 * @author Bruno Salmon
 */
public class SwingLineViewer
        <N extends Line, NB extends LineViewerBase<N, NB, NM>, NM extends LineViewerMixin<N, NB, NM>>

        extends SwingShapeViewer<N, NB, NM>
        implements LineViewerMixin<N, NB, NM> {

    public SwingLineViewer() {
        this((NB) new LineViewerBase());
    }

    public SwingLineViewer(NB base) {
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
