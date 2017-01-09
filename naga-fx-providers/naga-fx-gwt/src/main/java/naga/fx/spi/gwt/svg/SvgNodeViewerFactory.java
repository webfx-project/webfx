package naga.fx.spi.gwt.svg;

import naga.fx.scene.Group;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.CheckBox;
import naga.fx.scene.control.TextField;
import naga.fx.scene.layout.*;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Rectangle;
import naga.fx.scene.text.Text;
import naga.fx.spi.gwt.html.viewer.HtmlButtonViewer;
import naga.fx.spi.gwt.html.viewer.HtmlCheckBoxViewer;
import naga.fx.spi.gwt.html.viewer.HtmlTextFieldViewer;
import naga.fx.spi.gwt.svg.view.*;
import naga.fx.spi.viewer.NodeViewer;
import naga.fx.spi.viewer.base.NodeViewerFactoryImpl;

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
        registerNodeViewerFactory(Button.class, HtmlButtonViewer::new); // Will be embed in a foreignObject
        registerNodeViewerFactory(CheckBox.class, HtmlCheckBoxViewer::new); // Will be embed in a foreignObject
        registerNodeViewerFactory(TextField.class, HtmlTextFieldViewer::new); // Will be embed in a foreignObject
    }

    @Override
    protected NodeViewer<Region> createDefaultRegionViewer(Region node) {
        return new SvgLayoutViewer<>();
    }
}
