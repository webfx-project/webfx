package naga.providers.toolkit.javafx.drawing;

import naga.providers.toolkit.javafx.drawing.view.*;
import naga.toolkit.drawing.shapes.impl.*;
import naga.toolkit.drawing.spi.impl.NodeViewFactoryImpl;

/**
 * @author Bruno Salmon
 */
class FxNodeViewFactory extends NodeViewFactoryImpl {

    final static FxNodeViewFactory SINGLETON = new FxNodeViewFactory();

    private FxNodeViewFactory() {
        registerNodeViewFactory(RectangleImpl.class, FxRectangleView::new);
        registerNodeViewFactory(CircleImpl.class, FxCircleView::new);
        registerNodeViewFactory(TextShapeImpl.class, FxTextShapeView::new);
        registerNodeViewFactory(GroupImpl.class, FxGroupView::new);
        registerNodeViewFactory(EmbedGuiNodeImpl.class, FxEmbedGuiNodeView::new);
    }
}
