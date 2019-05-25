package webfx.javafxgraphics.registry;

import webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.svg.SvgLayoutPeer;

public class SvgJavaFxGraphicsRegistry {

    static {
        NodePeerFactoryRegistry.registerDefaultRegionPeerFactory(node -> new SvgLayoutPeer<>());

/*
        registerGroup();
        registerRectangle();
        registerCircle();
        registerLine();
        registerText();
        registerImageView();
        registerCanvas();
        registerPath();
*/
    }

/*
    public static void registerGroup() {
        registerNodePeerFactory(Group.class, SvgGroupPeer::new);
    }

    public static void registerRectangle() {
        registerNodePeerFactory(Rectangle.class, SvgRectanglePeer::new);
    }

    public static void registerCircle() {
        registerNodePeerFactory(Circle.class, SvgCirclePeer::new);
    }

    public static void registerLine() {
    }

    public static void registerText() {
        registerNodePeerFactory(Text.class, SvgTextPeer::new);
    }

    public static void registerImageView() {
        registerNodePeerFactory(ImageView.class, HtmlImageViewPeer::new);
    }

    public static void registerCanvas() {
        registerNodePeerFactory(Canvas.class, HtmlCanvasPeer::new);
    }

    public static void registerPath() {
        registerNodePeerFactory(Path.class, SvgPathPeer::new);
    }
*/

}
