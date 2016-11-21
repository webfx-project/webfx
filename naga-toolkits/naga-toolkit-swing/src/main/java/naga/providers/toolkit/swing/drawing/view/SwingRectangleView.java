package naga.providers.toolkit.swing.drawing.view;

import naga.commons.util.Numbers;
import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.view.base.RectangleViewBase;
import naga.toolkit.drawing.spi.view.base.RectangleViewMixin;
import naga.toolkit.drawing.spi.view.base.RectangleViewMixin2;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Bruno Salmon
 */
public class SwingRectangleView
        extends SwingShapeView<Rectangle, RectangleViewBase, RectangleViewMixin>
        implements RectangleViewMixin2 {

    public SwingRectangleView() {
        super(new RectangleViewBase());
    }

    @Override
    public void updateWidth(Double width) {
        updateSwingShape();
    }

    @Override
    public void updateHeight(Double height) {
        updateSwingShape();
    }

    @Override
    public void updateArcWidth(Double arcWidth) {
        updateSwingShape();
    }

    @Override
    public void updateArcHeight(Double arcHeight) {
        updateSwingShape();
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
