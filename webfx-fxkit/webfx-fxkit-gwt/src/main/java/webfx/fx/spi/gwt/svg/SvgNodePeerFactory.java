package webfx.fx.spi.gwt.svg;

import emul.javafx.scene.Group;
import emul.javafx.scene.control.Button;
import emul.javafx.scene.control.CheckBox;
import emul.javafx.scene.control.TextField;
import emul.javafx.scene.layout.*;
import emul.javafx.scene.shape.Circle;
import emul.javafx.scene.shape.Rectangle;
import emul.javafx.scene.text.Text;
import webfx.fx.spi.gwt.html.peer.HtmlButtonPeer;
import webfx.fx.spi.gwt.html.peer.HtmlCheckBoxPeer;
import webfx.fx.spi.gwt.html.peer.HtmlTextFieldPeer;
import webfx.fx.spi.gwt.svg.peer.*;
import webfx.fx.spi.peer.NodePeer;
import webfx.fx.spi.peer.base.NodePeerFactoryImpl;

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
