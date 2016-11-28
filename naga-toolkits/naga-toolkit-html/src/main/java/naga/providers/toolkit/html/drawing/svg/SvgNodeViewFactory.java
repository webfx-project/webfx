package naga.providers.toolkit.html.drawing.svg;

import naga.providers.toolkit.html.drawing.html.view.HtmlButtonView;
import naga.providers.toolkit.html.drawing.svg.view.*;
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
class SvgNodeViewFactory extends NodeViewFactoryImpl {

    final static SvgNodeViewFactory SINGLETON = new SvgNodeViewFactory();

    private SvgNodeViewFactory() {
        registerNodeViewFactory(RectangleImpl.class, SvgRectangleView::new);
        registerNodeViewFactory(CircleImpl.class, SvgCircleView::new);
        registerNodeViewFactory(TextImpl.class, SvgTextView::new);
        registerNodeViewFactory(EmbedGuiNodeImpl.class, SvgEmbedGuiNodeView::new);
        registerNodeViewFactory(GroupImpl.class, SvgGroupView::new);
        registerNodeViewFactory(VBoxImpl.class, SvgRegionView::new);
        registerNodeViewFactory(HBoxImpl.class, SvgRegionView::new);
        registerNodeViewFactory(BorderPaneImpl.class, SvgRegionView::new);
        registerNodeViewFactory(ButtonImpl.class, HtmlButtonView::new); // Will be embed in a foreignObject
    }
}
