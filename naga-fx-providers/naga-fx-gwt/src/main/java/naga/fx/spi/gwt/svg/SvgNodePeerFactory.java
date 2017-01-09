package naga.fx.spi.gwt.svg;

import naga.fx.scene.Group;
import naga.fx.scene.control.Button;
import naga.fx.scene.control.CheckBox;
import naga.fx.scene.control.TextField;
import naga.fx.scene.layout.*;
import naga.fx.scene.shape.Circle;
import naga.fx.scene.shape.Rectangle;
import naga.fx.scene.text.Text;
import naga.fx.spi.gwt.html.peer.HtmlButtonPeer;
import naga.fx.spi.gwt.html.peer.HtmlCheckBoxPeer;
import naga.fx.spi.gwt.html.peer.HtmlTextFieldPeer;
import naga.fx.spi.gwt.svg.peer.*;
import naga.fx.spi.peer.NodePeer;
import naga.fx.spi.peer.base.NodePeerFactoryImpl;

/**
 * @author Bruno Salmon
 */
class SvgNodePeerFactory extends NodePeerFactoryImpl {

    final static SvgNodePeerFactory SINGLETON = new SvgNodePeerFactory();

    private SvgNodePeerFactory() {
        registerNodePeerFactory(Rectangle.class, SvgRectanglePeer::new);
        registerNodePeerFactory(Circle.class, SvgCirclePeer::new);
        registerNodePeerFactory(Text.class, SvgTextPeer::new);
        registerNodePeerFactory(Group.class, SvgGroupPeer::new);
        registerNodePeerFactory(Button.class, HtmlButtonPeer::new); // Will be embed in a foreignObject
        registerNodePeerFactory(CheckBox.class, HtmlCheckBoxPeer::new); // Will be embed in a foreignObject
        registerNodePeerFactory(TextField.class, HtmlTextFieldPeer::new); // Will be embed in a foreignObject
    }

    @Override
    protected NodePeer<Region> createDefaultRegionPeer(Region node) {
        return new SvgLayoutPeer<>();
    }
}
