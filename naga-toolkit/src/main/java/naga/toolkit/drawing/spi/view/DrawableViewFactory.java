package naga.toolkit.drawing.spi.view;

import naga.toolkit.drawing.shapes.Drawable;

/**
 * @author Bruno Salmon
 */
public interface DrawableViewFactory {

    <D extends Drawable, V extends DrawableView<D>> V createDrawableView(D drawableInterface);

}
