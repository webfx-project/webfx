package naga.toolkit.drawing.spi;

import naga.toolkit.drawing.shapes.Drawable;
import naga.toolkit.drawing.shapes.DrawableParent;

/**
 * @author Bruno Salmon
 */
public interface DrawingNotifier {

    void onDrawableParentChange(DrawableParent drawableParent);

    void requestDrawableRepaint(Drawable drawable);

}
