package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.Rectangle;
import naga.toolkit.drawing.spi.view.base.RectangleViewBase;
import naga.toolkit.drawing.spi.view.base.RectangleViewMixin;

import java.awt.*;
import java.awt.geom.Rectangle2D;
import java.awt.geom.RoundRectangle2D;

/**
 * @author Bruno Salmon
 */
public class SwingRectangleView
        extends SwingShapeView<Rectangle, RectangleViewBase, RectangleViewMixin>
        implements RectangleViewMixin {

    public SwingRectangleView() {
        super(new RectangleViewBase());
    }

    @Override
    public void updateX(Double x) {

    }

    @Override
    public void updateY(Double y) {

    }

    @Override
    public void updateWidth(Double width) {

    }

    @Override
    public void updateHeight(Double height) {

    }

    @Override
    public void updateArcWidth(Double arcWidth) {

    }

    @Override
    public void updateArcHeight(Double arcHeight) {

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
        Rectangle r = getDrawable();
        prepareGraphics(g);
        paintSwingShape(r.getWidth(), r.getHeight(), g);
    }
}
