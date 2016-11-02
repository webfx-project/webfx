package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.view.base.DrawableViewBase;
import naga.toolkit.drawing.spi.view.base.DrawableViewImpl;
import naga.toolkit.drawing.spi.view.base.DrawableViewMixin;

import java.awt.*;


/**
 * @author Bruno Salmon
 */
public abstract class SwingDrawableView
        <D extends Drawable, DV extends DrawableViewBase<D, DV, DM>, DM extends DrawableViewMixin<D, DV, DM>>
        extends DrawableViewImpl<D, DV, DM> {

    SwingDrawableView(DV base) {
        super(base);
    }

    public abstract void paint(Graphics2D g);

}
