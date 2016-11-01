package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.SwingRectangleView;
import naga.providers.toolkit.swing.drawing.view.SwingTextShapeView;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.shapes.impl.TextShapeImpl;
import naga.toolkit.drawing.spi.impl.DrawableViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SwingDrawableViewFactory extends DrawableViewFactoryImpl {

    final static SwingDrawableViewFactory SINGLETON = new SwingDrawableViewFactory();

    SwingDrawableViewFactory() {
        registerDrawableViewFactory(RectangleImpl.class, SwingRectangleView::new);
        registerDrawableViewFactory(TextShapeImpl.class, SwingTextShapeView::new);
    }
}
