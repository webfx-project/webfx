package naga.providers.toolkit.swing.fx.viewer;

import naga.commons.util.Numbers;
import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.spi.viewer.base.RectangleViewerBase;
import naga.toolkit.fx.spi.viewer.base.RectangleViewerMixin;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Bruno Salmon
 */
public class SwingRectangleViewer
        extends SwingShapeViewer<Rectangle, RectangleViewerBase, RectangleViewerMixin>
        implements RectangleViewerMixin {

    public SwingRectangleViewer() {
        super(new RectangleViewerBase());
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
