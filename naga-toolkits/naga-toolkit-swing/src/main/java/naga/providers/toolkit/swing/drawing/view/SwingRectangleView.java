package naga.providers.toolkit.swing.drawing.view;

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
    protected Shape createSwingShape(Graphics2D g) {
        Rectangle r = getDrawable();
        Double arcWidth = r.getArcWidth();
        Double arcHeight = r.getArcHeight();
        if (arcWidth != null && arcHeight != null && arcWidth != 0 && arcHeight != 0)
            return new RoundRectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight(), arcWidth, arcHeight);
        return new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public void paint(Graphics2D g) {
        prepareGraphics(g);
        Rectangle r = getDrawable();
        paintSwingShape(r.getWidth(), r.getHeight(), g);
    }
}
