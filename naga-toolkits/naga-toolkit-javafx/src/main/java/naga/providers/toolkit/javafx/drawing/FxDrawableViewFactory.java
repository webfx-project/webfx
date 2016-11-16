package naga.providers.toolkit.javafx.drawing;

import naga.providers.toolkit.javafx.drawing.view.FxEmbedDrawableView;
import naga.providers.toolkit.javafx.drawing.view.FxGroupView;
import naga.providers.toolkit.javafx.drawing.view.FxRectangleView;
import naga.providers.toolkit.javafx.drawing.view.FxTextShapeView;
import naga.toolkit.drawing.shapes.impl.EmbedDrawableImpl;
import naga.toolkit.drawing.shapes.impl.GroupImpl;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.shapes.impl.TextShapeImpl;
import naga.toolkit.drawing.spi.impl.DrawableViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class FxDrawableViewFactory extends DrawableViewFactoryImpl {

    final static FxDrawableViewFactory SINGLETON = new FxDrawableViewFactory();

    FxDrawableViewFactory() {
        registerDrawableViewFactory(RectangleImpl.class, FxRectangleView::new);
        registerDrawableViewFactory(TextShapeImpl.class, FxTextShapeView::new);
        registerDrawableViewFactory(GroupImpl.class, FxGroupView::new);
        registerDrawableViewFactory(EmbedDrawableImpl.class, FxEmbedDrawableView::new);
    }
}
