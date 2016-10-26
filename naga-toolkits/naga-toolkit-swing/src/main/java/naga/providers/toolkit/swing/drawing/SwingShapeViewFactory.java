package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.SwingRectangleView;
import naga.providers.toolkit.swing.drawing.view.SwingTextShapeView;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.shapes.impl.TextShapeImpl;
import naga.toolkit.drawing.spi.impl.ShapeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SwingShapeViewFactory extends ShapeViewFactoryImpl {

    final static SwingShapeViewFactory SINGLETON = new SwingShapeViewFactory();

    SwingShapeViewFactory() {
        registerShapeViewFactory(RectangleImpl.class, SwingRectangleView::new);
        registerShapeViewFactory(TextShapeImpl.class, SwingTextShapeView::new);
    }
}
