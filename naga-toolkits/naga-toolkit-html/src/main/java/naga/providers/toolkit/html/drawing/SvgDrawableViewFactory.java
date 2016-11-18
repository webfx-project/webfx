package naga.providers.toolkit.html.drawing;

import naga.providers.toolkit.html.drawing.view.*;
import naga.toolkit.drawing.shapes.impl.*;
import naga.toolkit.drawing.spi.impl.DrawableViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SvgDrawableViewFactory extends DrawableViewFactoryImpl {

    final static SvgDrawableViewFactory SINGLETON = new SvgDrawableViewFactory();

    SvgDrawableViewFactory() {
        registerDrawableViewFactory(RectangleImpl.class, SvgRectangleView::new);
        registerDrawableViewFactory(CircleImpl.class, SvgCircleView::new);
        registerDrawableViewFactory(TextShapeImpl.class, SvgTextShapeView::new);
        registerDrawableViewFactory(GroupImpl.class, SvgGroupView::new);
        registerDrawableViewFactory(EmbedDrawableImpl.class, SvgEmbedDrawingView::new);
    }
}
