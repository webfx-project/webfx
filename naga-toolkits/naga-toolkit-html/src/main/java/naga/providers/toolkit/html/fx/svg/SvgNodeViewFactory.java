package naga.providers.toolkit.html.fx.svg;

import naga.providers.toolkit.html.fx.html.view.HtmlButtonView;
import naga.providers.toolkit.html.fx.html.view.HtmlCheckBoxView;
import naga.providers.toolkit.html.fx.html.view.HtmlTextFieldView;
import naga.providers.toolkit.html.fx.svg.view.*;
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
import naga.toolkit.fx.scene.text.impl.TextImpl;

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
        registerNodeViewFactory(FlowPaneImpl.class, SvgRegionView::new);
        registerNodeViewFactory(ButtonImpl.class, HtmlButtonView::new); // Will be embed in a foreignObject
        registerNodeViewFactory(CheckBoxImpl.class, HtmlCheckBoxView::new); // Will be embed in a foreignObject
        registerNodeViewFactory(TextFieldImpl.class, HtmlTextFieldView::new); // Will be embed in a foreignObject
    }
}
