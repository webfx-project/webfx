package naga.providers.toolkit.html.fx.html;

import naga.providers.toolkit.html.fx.html.view.*;
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
class HtmlNodeViewFactory extends NodeViewFactoryImpl {

    final static HtmlNodeViewFactory SINGLETON = new HtmlNodeViewFactory();

    private HtmlNodeViewFactory() {
        registerNodeViewFactory(RectangleImpl.class, HtmlRectangleView::new);
        registerNodeViewFactory(CircleImpl.class, HtmlCircleView::new);
        registerNodeViewFactory(TextImpl.class, HtmlTextView::new);
        registerNodeViewFactory(EmbedGuiNodeImpl.class, HtmlEmbedGuiNodeView::new);
        registerNodeViewFactory(GroupImpl.class, HtmlGroupView::new);
        registerNodeViewFactory(VBoxImpl.class, HtmlLayoutView::new);
        registerNodeViewFactory(HBoxImpl.class, HtmlLayoutView::new);
        registerNodeViewFactory(BorderPaneImpl.class, HtmlLayoutView::new);
        registerNodeViewFactory(FlowPaneImpl.class, HtmlLayoutView::new);
        registerNodeViewFactory(ButtonImpl.class, HtmlButtonView::new);
        registerNodeViewFactory(CheckBoxImpl.class, HtmlCheckBoxView::new);
        registerNodeViewFactory(TextFieldImpl.class, HtmlTextFieldView::new);
    }
}
