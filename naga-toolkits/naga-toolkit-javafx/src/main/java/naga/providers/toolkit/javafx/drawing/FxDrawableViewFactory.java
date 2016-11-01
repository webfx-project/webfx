package naga.providers.toolkit.javafx.drawing;

import naga.providers.toolkit.javafx.drawing.view.FxRectangleView;
import naga.providers.toolkit.javafx.drawing.view.FxTextShapeView;
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
    }
}
