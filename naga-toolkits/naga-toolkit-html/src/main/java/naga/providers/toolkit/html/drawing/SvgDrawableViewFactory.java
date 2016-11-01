package naga.providers.toolkit.html.drawing;

import naga.providers.toolkit.html.drawing.view.SvgGroupView;
import naga.providers.toolkit.html.drawing.view.SvgRectangleView;
import naga.providers.toolkit.html.drawing.view.SvgTextShapeView;
import naga.toolkit.drawing.shapes.impl.GroupImpl;
import naga.toolkit.drawing.shapes.impl.RectangleImpl;
import naga.toolkit.drawing.shapes.impl.TextShapeImpl;
import naga.toolkit.drawing.spi.impl.DrawableViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SvgDrawableViewFactory extends DrawableViewFactoryImpl {

    final static SvgDrawableViewFactory SINGLETON = new SvgDrawableViewFactory();

    SvgDrawableViewFactory() {
        registerDrawableViewFactory(RectangleImpl.class, SvgRectangleView::new);
        registerDrawableViewFactory(TextShapeImpl.class, SvgTextShapeView::new);
        registerDrawableViewFactory(GroupImpl.class, SvgGroupView::new);
    }
}
