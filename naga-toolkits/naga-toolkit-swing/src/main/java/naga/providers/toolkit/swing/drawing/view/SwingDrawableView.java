package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.impl.canvas.CanvasDrawableView;
import naga.toolkit.drawing.spi.view.base.DrawableViewBase;
import naga.toolkit.drawing.spi.view.base.DrawableViewImpl;
import naga.toolkit.drawing.spi.view.base.DrawableViewMixin;
import naga.toolkit.transform.Transform;

import java.awt.*;
import java.awt.geom.AffineTransform;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public abstract class SwingDrawableView
        <D extends Drawable, DV extends DrawableViewBase<D, DV, DM>, DM extends DrawableViewMixin<D, DV, DM>>

        extends DrawableViewImpl<D, DV, DM>
        implements CanvasDrawableView<D, Graphics2D> {

    private AffineTransform swingTransform;

    SwingDrawableView(DV base) {
        super(base);
    }

    @Override
    public void updateTransforms(List<Transform> transforms) {
    }

    @Override
    public void paint(Graphics2D g) {
        prepareGraphics(g);
    }

    void prepareGraphics(Graphics2D g) {
        if (swingTransform != null) {
            AffineTransform tx = new AffineTransform(g.getTransform());
            tx.concatenate(swingTransform);
            g.setTransform(tx);
        }
    }

}
