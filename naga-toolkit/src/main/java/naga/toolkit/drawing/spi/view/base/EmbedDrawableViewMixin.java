package naga.toolkit.drawing.spi.view.base;

import naga.toolkit.drawing.shapes.EmbedDrawable;
import naga.toolkit.drawing.spi.view.EmbedDrawableView;

/**
 * @author Bruno Salmon
 */
public interface EmbedDrawableViewMixin
        extends EmbedDrawableView,
        DrawableViewMixin<EmbedDrawable, EmbedDrawableViewBase, EmbedDrawableViewMixin> {

}
