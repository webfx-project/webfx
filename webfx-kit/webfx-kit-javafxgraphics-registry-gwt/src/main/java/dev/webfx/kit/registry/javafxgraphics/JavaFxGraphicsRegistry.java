package dev.webfx.kit.registry.javafxgraphics;

import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.scene.text.Text;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.*;

import static dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.*;


public class JavaFxGraphicsRegistry {

    public static void registerGroup() {
        NodePeerFactoryRegistry.registerDefaultGroupPeerFactory(node -> {
            String tag = requestedCustomTag(node);
            if (tag == null) {
                String classTag = classTag(node);
                // Hot registration for future cases
                registerNodePeerFactory(node.getClass(), () -> new HtmlGroupPeer<>(classTag));
                tag = classTag;
            }
            return new HtmlGroupPeer<>(tag);
        });
    }

    public static void registerRegion() {
        NodePeerFactoryRegistry.registerDefaultRegionPeerFactory(node -> {
            String tag = requestedCustomTag(node);
            if (tag == null) {
                String classTag = classTag(node);
                // Hot registration for future cases
                registerNodePeerFactory(node.getClass(), () -> new HtmlLayoutPeer<>(classTag));
                tag = classTag;
            }
            return new HtmlLayoutPeer<>(tag);
        });
    }

    public static void registerRectangle() {
        registerNodePeerFactory(Rectangle.class, HtmlRectanglePeer::new);
        registerCustomTagNodePeerFactory(Rectangle.class, HtmlRectanglePeer::new);
    }

    public static void registerArc() {
        registerNodePeerFactory(Arc.class, HtmlArcPeer::new);
    }

    public static void registerCircle() {
        registerNodePeerFactory(Circle.class, HtmlCirclePeer::new);
    }

    public static void registerLine() {
        registerNodePeerFactory(Line.class, HtmlLinePeer::new);
    }

    public static void registerText() {
        registerNodePeerFactory(Text.class, HtmlTextPeer::new);
    }

    public static void registerImageView() {
        registerNodePeerFactory(ImageView.class, HtmlImageViewPeer::new);
    }

    public static void registerCanvas() {
        registerNodePeerFactory(Canvas.class, HtmlCanvasPeer::new);
    }

    public static void registerPath() {
        registerNodePeerFactory(Path.class, HtmlPathPeer::new);
    }

    public static void registerSVGPath() {
        registerNodePeerFactory(SVGPath.class, HtmlSVGPathPeer::new);
    }

    /**
     * Create the peer associated with the Robot class. The implementation must return an instance of the interface
     * javafx.scene.robot.RobotPeer declared in the webfx-kit-javafxgraphics-emul module. The signature below says it
     * returns an Object, but it's to avoid a circular dependency between the 2 modules. Conceptually it should be
     * RobotPeer.
     *
     * @return an instance of RobotPeer
     */
    public static Object createRobotPeer() {
        return new GwtRobotPeer();
    }

}
