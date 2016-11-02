package naga.providers.toolkit.swing.drawing.view;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.spi.impl.canvas.CanvasDrawableView;
import naga.toolkit.drawing.spi.view.base.DrawableViewBase;
import naga.toolkit.drawing.spi.view.base.DrawableViewImpl;
import naga.toolkit.drawing.spi.view.base.DrawableViewMixin;

import java.awt.*;


/**
 * @author Bruno Salmon
 */
public abstract class SwingDrawableView
        <D extends Drawable, DV extends DrawableViewBase<D, DV, DM>, DM extends DrawableViewMixin<D, DV, DM>>

        extends DrawableViewImpl<D, DV, DM>
        implements CanvasDrawableView<D, Graphics2D> {

    SwingDrawableView(DV base) {
        super(base);
    }

}
