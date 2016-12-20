package naga.providers.toolkit.html.fx.svg;

import naga.providers.toolkit.html.fx.html.viewer.HtmlButtonViewer;
import naga.providers.toolkit.html.fx.html.viewer.HtmlCheckBoxViewer;
import naga.providers.toolkit.html.fx.html.viewer.HtmlTextFieldViewer;
import naga.providers.toolkit.html.fx.svg.view.*;
import naga.toolkit.fx.scene.control.Button;
import naga.toolkit.fx.scene.control.CheckBox;
import naga.toolkit.fx.scene.control.TextField;
import naga.toolkit.fx.scene.Group;
import naga.toolkit.fx.scene.layout.BorderPane;
import naga.toolkit.fx.scene.layout.FlowPane;
import naga.toolkit.fx.scene.layout.HBox;
import naga.toolkit.fx.scene.layout.VBox;
import naga.toolkit.fx.scene.shape.Circle;
import naga.toolkit.fx.scene.shape.Rectangle;
import naga.toolkit.fx.scene.text.Text;
import naga.toolkit.fx.spi.viewer.base.NodeViewerFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SvgNodeViewerFactory extends NodeViewerFactoryImpl {

    final static SvgNodeViewerFactory SINGLETON = new SvgNodeViewerFactory();

    private SvgNodeViewerFactory() {
        registerNodeViewerFactory(Rectangle.class, SvgRectangleViewer::new);
        registerNodeViewerFactory(Circle.class, SvgCircleViewer::new);
        registerNodeViewerFactory(Text.class, SvgTextViewer::new);
        registerNodeViewerFactory(Group.class, SvgGroupViewer::new);
        registerNodeViewerFactory(VBox.class, SvgLayoutViewer::new);
        registerNodeViewerFactory(HBox.class, SvgLayoutViewer::new);
        registerNodeViewerFactory(BorderPane.class, SvgLayoutViewer::new);
        registerNodeViewerFactory(FlowPane.class, SvgLayoutViewer::new);
        registerNodeViewerFactory(Button.class, HtmlButtonViewer::new); // Will be embed in a foreignObject
        registerNodeViewerFactory(CheckBox.class, HtmlCheckBoxViewer::new); // Will be embed in a foreignObject
        registerNodeViewerFactory(TextField.class, HtmlTextFieldViewer::new); // Will be embed in a foreignObject
    }
}
