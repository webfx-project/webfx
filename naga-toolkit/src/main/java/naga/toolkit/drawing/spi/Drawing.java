package naga.toolkit.drawing.spi;

import naga.toolkit.drawing.shapes.DrawableParent;
import naga.toolkit.drawing.spi.view.DrawableViewFactory;

/**
 * @author Bruno Salmon
 */
public interface Drawing extends DrawableParent {

    void setDrawableViewFactory(DrawableViewFactory drawableViewFactory);

}
