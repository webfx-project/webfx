package naga.providers.toolkit.html.drawing;

import naga.providers.toolkit.html.drawing.view.SvgRectangleView;
import naga.providers.toolkit.html.drawing.view.SvgTextShapeView;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.shapes.impl.TextShapeImpl;
import naga.toolkit.drawing.spi.impl.ShapeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SvgShapeViewFactory extends ShapeViewFactoryImpl {

    final static SvgShapeViewFactory SINGLETON = new SvgShapeViewFactory();

    SvgShapeViewFactory() {
        registerShapeViewFactory(RectangleImpl.class, SvgRectangleView::new);
        registerShapeViewFactory(TextShapeImpl.class, SvgTextShapeView::new);
    }
}
