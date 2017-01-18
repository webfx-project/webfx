package naga.fx.spi.swing.peer;

import naga.commons.util.Numbers;
import emul.javafx.scene.shape.Rectangle;
import naga.fx.spi.peer.base.RectanglePeerBase;
import naga.fx.spi.peer.base.RectanglePeerMixin;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Bruno Salmon
 */
public class SwingRectanglePeer
        <N extends Rectangle, NB extends RectanglePeerBase<N, NB, NM>, NM extends RectanglePeerMixin<N, NB, NM>>

        extends SwingShapePeer<N, NB, NM>
        implements RectanglePeerMixin<N, NB, NM> {

    public SwingRectanglePeer() {
        this((NB) new RectanglePeerBase());
    }

    public SwingRectanglePeer(NB base) {
        super(base);
    }

    @Override
    public void updateWidth(Double width) {
        invalidateSwingShape();
    }

    @Override
    public void updateHeight(Double height) {
        invalidateSwingShape();
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        invalidateSwingShape();
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        invalidateSwingShape();
    }

    @Override
    public void updateX(Double x) {
        // Doesn't affect the shape, x will be used in prepareCanvasContext() translation
    }

    @Override
    public void updateY(Double y) {
        // Doesn't affect the shape, y will be used in prepareCanvasContext() translation
    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        Rectangle r = getNode();
        double arcWidth = Numbers.doubleValue(r.getArcWidth());
        double arcHeight = Numbers.doubleValue(r.getArcHeight());
        if (arcWidth != 0 && arcHeight != 0)
            return new RoundRectangle2D.Double(0, 0, r.getWidth(), r.getHeight(), arcWidth, arcHeight);
        return new Rectangle2D.Double(0, 0, r.getWidth(), r.getHeight());
    }

    @Override
    public void prepareCanvasContext(Graphics2D g) {
        super.prepareCanvasContext(g);
        Rectangle r = getNode();
        g.translate(r.getX(), r.getY());
    }

    @Override
    public void paint(Graphics2D g) {
        Rectangle r = getNode();
        paintSwingShape(r.getWidth(), r.getHeight(), g);
    }
}
