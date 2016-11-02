package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.view.base.RectangleViewBase;
import naga.toolkit.drawing.spi.view.mixin.RectangleViewMixin;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Bruno Salmon
 */
public class SwingRectangleView extends SwingShapeView<Rectangle> implements RectangleViewMixin {

    private final RectangleViewBase base = new RectangleViewBase();
    @Override
    public RectangleViewBase getDrawableViewBase() {
        return base;
    }

    @Override
    protected Shape createSwingShape(Graphics2D g) {
        Rectangle r = drawable;
        Double arcWidth = r.getArcWidth();
        Double arcHeight = r.getArcHeight();
        if (arcWidth != null && arcHeight != null && arcWidth != 0 && arcHeight != 0)
            return new RoundRectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight(), arcWidth, arcHeight);
        return new Rectangle2D.Double(r.getX(), r.getY(), r.getWidth(), r.getHeight());
    }

    @Override
    public void paint(Graphics2D g) {
        prepareGraphics(g);
        paintSwingShape(drawable.getWidth(), drawable.getHeight(), g);
    }
}
