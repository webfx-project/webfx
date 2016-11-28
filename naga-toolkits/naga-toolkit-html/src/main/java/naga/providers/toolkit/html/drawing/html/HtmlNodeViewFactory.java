package naga.providers.toolkit.html.drawing.html;

import naga.providers.toolkit.html.drawing.html.view.*;
import naga.toolkit.drawing.layout.impl.BorderPaneImpl;
import naga.toolkit.drawing.layout.impl.HBoxImpl;
import naga.toolkit.drawing.layout.impl.VBoxImpl;
import naga.toolkit.drawing.scene.control.impl.ButtonImpl;
import naga.toolkit.drawing.scene.impl.EmbedGuiNodeImpl;
import naga.toolkit.drawing.scene.impl.GroupImpl;
import naga.toolkit.drawing.shape.impl.CircleImpl;
import naga.toolkit.drawing.shape.impl.RectangleImpl;
import naga.toolkit.drawing.spi.impl.NodeViewFactoryImpl;
import naga.toolkit.drawing.text.impl.TextImpl;

/**
 * @author Bruno Salmon
 */
class HtmlNodeViewFactory extends NodeViewFactoryImpl {

    final static HtmlNodeViewFactory SINGLETON = new HtmlNodeViewFactory();

    private HtmlNodeViewFactory() {
        registerNodeViewFactory(RectangleImpl.class, HtmlRectangleView::new);
        registerNodeViewFactory(CircleImpl.class, HtmlCircleView::new);
        registerNodeViewFactory(TextImpl.class, HtmlTextView::new);
        registerNodeViewFactory(EmbedGuiNodeImpl.class, HtmlEmbedGuiNodeView::new);
        registerNodeViewFactory(GroupImpl.class, HtmlGroupView::new);
        registerNodeViewFactory(VBoxImpl.class, HtmlRegionView::new);
        registerNodeViewFactory(HBoxImpl.class, HtmlRegionView::new);
        registerNodeViewFactory(BorderPaneImpl.class, HtmlRegionView::new);
        registerNodeViewFactory(ButtonImpl.class, HtmlButtonView::new);
    }
}
