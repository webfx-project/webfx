package naga.providers.toolkit.html.drawing;

import naga.providers.toolkit.html.drawing.view.*;
import naga.toolkit.drawing.shapes.impl.*;
import naga.toolkit.drawing.spi.impl.NodeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SvgNodeViewFactory extends NodeViewFactoryImpl {

    final static SvgNodeViewFactory SINGLETON = new SvgNodeViewFactory();

    private SvgNodeViewFactory() {
        registerNodeViewFactory(RectangleImpl.class, SvgRectangleView::new);
        registerNodeViewFactory(CircleImpl.class, SvgCircleView::new);
        registerNodeViewFactory(TextShapeImpl.class, SvgTextShapeView::new);
        registerNodeViewFactory(GroupImpl.class, SvgGroupView::new);
        registerNodeViewFactory(EmbedGuiNodeImpl.class, SvgEmbedGuiNodeView::new);
    }
}
