package naga.providers.toolkit.swing.fx;

import naga.providers.toolkit.swing.fx.view.*;
import naga.toolkit.fx.scene.control.impl.ButtonImpl;
import naga.toolkit.fx.scene.control.impl.CheckBoxImpl;
import naga.toolkit.fx.scene.control.impl.TextFieldImpl;
import naga.toolkit.fx.scene.impl.EmbedGuiNodeImpl;
import naga.toolkit.fx.scene.impl.GroupImpl;
import naga.toolkit.fx.scene.layout.impl.BorderPaneImpl;
import naga.toolkit.fx.scene.layout.impl.FlowPaneImpl;
import naga.toolkit.fx.scene.layout.impl.HBoxImpl;
import naga.toolkit.fx.scene.layout.impl.VBoxImpl;
import naga.toolkit.fx.scene.shape.impl.CircleImpl;
import naga.toolkit.fx.scene.shape.impl.RectangleImpl;
import naga.toolkit.fx.spi.impl.NodeViewFactoryImpl;
import naga.toolkit.fx.text.impl.TextImpl;

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
        registerNodeViewFactory(TextFieldImpl.class, SwingTextFieldView::new);
        registerNodeViewFactory(CheckBoxImpl.class, SwingCheckBoxView::new);
    }
}
