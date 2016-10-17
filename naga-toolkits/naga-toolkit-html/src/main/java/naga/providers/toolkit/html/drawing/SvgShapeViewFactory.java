package naga.providers.toolkit.html.drawing;

import naga.providers.toolkit.html.drawing.view.SvgRectangleView;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.spi.impl.ShapeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
public class SvgShapeViewFactory extends ShapeViewFactoryImpl {

    public final static SvgShapeViewFactory SINGLETON = new SvgShapeViewFactory();

    public SvgShapeViewFactory() {
        registerShapeViewFactory(RectangleImpl.class, SvgRectangleView::new);
    }
}
