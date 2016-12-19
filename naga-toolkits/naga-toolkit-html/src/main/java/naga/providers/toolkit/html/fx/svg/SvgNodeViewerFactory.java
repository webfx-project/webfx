package naga.providers.toolkit.html.fx.svg;

import naga.providers.toolkit.html.fx.html.viewer.HtmlButtonViewer;
import naga.providers.toolkit.html.fx.html.viewer.HtmlCheckBoxViewer;
import naga.providers.toolkit.html.fx.html.viewer.HtmlTextFieldViewer;
import naga.providers.toolkit.html.fx.svg.view.*;
import naga.toolkit.fx.scene.control.impl.ButtonImpl;
import naga.toolkit.fx.scene.control.impl.CheckBoxImpl;
import naga.toolkit.fx.scene.control.impl.TextFieldImpl;
import naga.toolkit.fx.scene.impl.GroupImpl;
import naga.toolkit.fx.scene.layout.impl.BorderPaneImpl;
import naga.toolkit.fx.scene.layout.impl.FlowPaneImpl;
import naga.toolkit.fx.scene.layout.impl.HBoxImpl;
import naga.toolkit.fx.scene.layout.impl.VBoxImpl;
import naga.toolkit.fx.scene.shape.impl.CircleImpl;
import naga.toolkit.fx.scene.shape.impl.RectangleImpl;
import naga.toolkit.fx.scene.text.impl.TextImpl;
import naga.toolkit.fx.spi.impl.NodeViewerFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SvgNodeViewerFactory extends NodeViewerFactoryImpl {

    final static SvgNodeViewerFactory SINGLETON = new SvgNodeViewerFactory();

    private SvgNodeViewerFactory() {
        registerNodeViewerFactory(RectangleImpl.class, SvgRectangleViewer::new);
        registerNodeViewerFactory(CircleImpl.class, SvgCircleViewer::new);
        registerNodeViewerFactory(TextImpl.class, SvgTextViewer::new);
        registerNodeViewerFactory(GroupImpl.class, SvgGroupViewer::new);
        registerNodeViewerFactory(VBoxImpl.class, SvgLayoutViewer::new);
        registerNodeViewerFactory(HBoxImpl.class, SvgLayoutViewer::new);
        registerNodeViewerFactory(BorderPaneImpl.class, SvgLayoutViewer::new);
        registerNodeViewerFactory(FlowPaneImpl.class, SvgLayoutViewer::new);
        registerNodeViewerFactory(ButtonImpl.class, HtmlButtonViewer::new); // Will be embed in a foreignObject
        registerNodeViewerFactory(CheckBoxImpl.class, HtmlCheckBoxViewer::new); // Will be embed in a foreignObject
        registerNodeViewerFactory(TextFieldImpl.class, HtmlTextFieldViewer::new); // Will be embed in a foreignObject
    }
}
