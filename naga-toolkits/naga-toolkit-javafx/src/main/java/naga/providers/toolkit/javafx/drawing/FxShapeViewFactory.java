package naga.providers.toolkit.javafx.drawing;

import naga.providers.toolkit.javafx.drawing.view.FxRectangleView;
import naga.providers.toolkit.javafx.drawing.view.FxTextShapeView;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.shapes.impl.TextShapeImpl;
import naga.toolkit.drawing.spi.impl.ShapeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class FxShapeViewFactory extends ShapeViewFactoryImpl {

    final static FxShapeViewFactory SINGLETON = new FxShapeViewFactory();

    FxShapeViewFactory() {
        registerShapeViewFactory(RectangleImpl.class, FxRectangleView::new);
        registerShapeViewFactory(TextShapeImpl.class, FxTextShapeView::new);
    }
}
