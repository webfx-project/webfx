package naga.providers.toolkit.javafx.drawing;

import naga.providers.toolkit.javafx.drawing.view.*;
import naga.toolkit.drawing.shapes.impl.*;
import naga.toolkit.drawing.spi.impl.DrawableViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class FxDrawableViewFactory extends DrawableViewFactoryImpl {

    final static FxDrawableViewFactory SINGLETON = new FxDrawableViewFactory();

    FxDrawableViewFactory() {
        registerDrawableViewFactory(RectangleImpl.class, FxRectangleView::new);
        registerDrawableViewFactory(CircleImpl.class, FxCircleView::new);
        registerDrawableViewFactory(TextShapeImpl.class, FxTextShapeView::new);
        registerDrawableViewFactory(GroupImpl.class, FxGroupView::new);
        registerDrawableViewFactory(EmbedDrawableImpl.class, FxEmbedDrawableView::new);
    }
}
