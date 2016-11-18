package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.*;
import naga.toolkit.drawing.shapes.impl.*;
import naga.toolkit.drawing.spi.impl.DrawableViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SwingDrawableViewFactory extends DrawableViewFactoryImpl {

    final static SwingDrawableViewFactory SINGLETON = new SwingDrawableViewFactory();

    SwingDrawableViewFactory() {
        registerDrawableViewFactory(RectangleImpl.class, SwingRectangleView::new);
        registerDrawableViewFactory(CircleImpl.class, SwingCircleView::new);
        registerDrawableViewFactory(TextShapeImpl.class, SwingTextShapeView::new);
        registerDrawableViewFactory(GroupImpl.class, SwingGroupView::new);
        registerDrawableViewFactory(EmbedDrawableImpl.class, SwingEmbedDrawableView::new);
    }
}
