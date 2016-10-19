package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.SwingRectangleView;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.spi.impl.ShapeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
public class SwingShapeViewFactory extends ShapeViewFactoryImpl {

    public final static SwingShapeViewFactory SINGLETON = new SwingShapeViewFactory();

    public SwingShapeViewFactory() {
        registerShapeViewFactory(RectangleImpl.class, SwingRectangleView::new);
    }
}
