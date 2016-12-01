package naga.providers.toolkit.swing.drawing;

import naga.providers.toolkit.swing.drawing.view.*;
import naga.toolkit.drawing.scene.control.impl.ButtonImpl;
import naga.toolkit.drawing.scene.impl.EmbedGuiNodeImpl;
import naga.toolkit.drawing.scene.impl.GroupImpl;
import naga.toolkit.drawing.scene.layout.impl.BorderPaneImpl;
import naga.toolkit.drawing.scene.layout.impl.FlowPaneImpl;
import naga.toolkit.drawing.scene.layout.impl.HBoxImpl;
import naga.toolkit.drawing.scene.layout.impl.VBoxImpl;
import naga.toolkit.drawing.shape.impl.CircleImpl;
import naga.toolkit.drawing.shape.impl.RectangleImpl;
import naga.toolkit.drawing.spi.impl.NodeViewFactoryImpl;
import naga.toolkit.drawing.text.impl.TextImpl;

/**
 * @author Bruno Salmon
 */
class SwingNodeViewFactory extends NodeViewFactoryImpl {

    final static SwingNodeViewFactory SINGLETON = new SwingNodeViewFactory();

    private SwingNodeViewFactory() {
        registerNodeViewFactory(RectangleImpl.class, SwingRectangleView::new);
        registerNodeViewFactory(CircleImpl.class, SwingCircleView::new);
        registerNodeViewFactory(TextImpl.class, SwingTextView::new);
        registerNodeViewFactory(EmbedGuiNodeImpl.class, SwingEmbedGuiNodeView::new);
        registerNodeViewFactory(GroupImpl.class, SwingGroupView::new);
        registerNodeViewFactory(VBoxImpl.class, SwingRegionView::new);
        registerNodeViewFactory(HBoxImpl.class, SwingRegionView::new);
        registerNodeViewFactory(BorderPaneImpl.class, SwingRegionView::new);
        registerNodeViewFactory(FlowPaneImpl.class, SwingRegionView::new);
        registerNodeViewFactory(ButtonImpl.class, SwingButtonView::new);
    }
}
