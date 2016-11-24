package naga.providers.toolkit.javafx.drawing;

import naga.providers.toolkit.javafx.drawing.view.*;
import naga.toolkit.drawing.layout.impl.HBoxImpl;
import naga.toolkit.drawing.layout.impl.VBoxImpl;
import naga.toolkit.drawing.scene.impl.EmbedGuiNodeImpl;
import naga.toolkit.drawing.scene.impl.GroupImpl;
import naga.toolkit.drawing.shape.impl.*;
import naga.toolkit.drawing.spi.impl.NodeViewFactoryImpl;
import naga.toolkit.drawing.text.impl.TextShapeImpl;

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
        registerNodeViewFactory(VBoxImpl.class, FxVBoxView::new);
        registerNodeViewFactory(HBoxImpl.class, FxHBoxView::new);
    }
}
