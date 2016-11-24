package naga.providers.toolkit.html.drawing;

import naga.providers.toolkit.html.drawing.view.*;
import naga.toolkit.drawing.layout.impl.BorderPaneImpl;
import naga.toolkit.drawing.layout.impl.HBoxImpl;
import naga.toolkit.drawing.layout.impl.VBoxImpl;
import naga.toolkit.drawing.scene.impl.EmbedGuiNodeImpl;
import naga.toolkit.drawing.scene.impl.GroupImpl;
import naga.toolkit.drawing.shape.impl.CircleImpl;
import naga.toolkit.drawing.shape.impl.RectangleImpl;
import naga.toolkit.drawing.spi.impl.NodeViewFactoryImpl;
import naga.toolkit.drawing.text.impl.TextShapeImpl;

/**
 * @author Bruno Salmon
 */
class SvgNodeViewFactory extends NodeViewFactoryImpl {

    final static SvgNodeViewFactory SINGLETON = new SvgNodeViewFactory();

    private SvgNodeViewFactory() {
        registerNodeViewFactory(RectangleImpl.class, SvgRectangleView::new);
        registerNodeViewFactory(CircleImpl.class, SvgCircleView::new);
        registerNodeViewFactory(TextShapeImpl.class, SvgTextShapeView::new);
        registerNodeViewFactory(EmbedGuiNodeImpl.class, SvgEmbedGuiNodeView::new);
        registerNodeViewFactory(GroupImpl.class, SvgGroupView::new);
        registerNodeViewFactory(VBoxImpl.class, SvgRegionView::new);
        registerNodeViewFactory(HBoxImpl.class, SvgRegionView::new);
        registerNodeViewFactory(BorderPaneImpl.class, SvgRegionView::new);
    }
}
