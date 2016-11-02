package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.Drawable;

/**
 * @author Bruno Salmon
 */
public abstract class DrawableViewImpl
        <D extends Drawable, DV extends DrawableViewBase<D, DV, DM>, DM extends DrawableViewMixin<D, DV, DM>>
        implements DrawableViewMixin<D, DV, DM> {

    private final DV base;

    public DrawableViewImpl(DV base) {
        this.base = base;
        base.setMixin((DM) this);
    }

    @Override
    public DV getDrawableViewBase() {
        return base;
    }

    public D getDrawable() {
        return base.getDrawable();
    }
}
