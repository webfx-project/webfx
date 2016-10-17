package naga.providers.toolkit.javafx.drawing;

import naga.providers.toolkit.javafx.drawing.view.FxRectangleView;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.spi.impl.ShapeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
public class FxShapeViewFactory extends ShapeViewFactoryImpl {

    public final static FxShapeViewFactory SINGLETON = new FxShapeViewFactory();

    public FxShapeViewFactory() {
        registerShapeViewFactory(RectangleImpl.class, FxRectangleView::new);
    }
}
