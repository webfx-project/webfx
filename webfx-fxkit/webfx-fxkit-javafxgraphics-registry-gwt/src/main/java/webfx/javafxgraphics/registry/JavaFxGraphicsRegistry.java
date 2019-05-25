package webfx.javafxgraphics.registry;

import javafx.scene.Group;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Line;
import javafx.scene.shape.Path;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Text;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.*;

import static webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry.registerNodePeerFactory;


public class JavaFxGraphicsRegistry {

    static {
        NodePeerFactoryRegistry.registerDefaultRegionPeerFactory(node -> new HtmlLayoutPeer<>());
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

    public static void registerGroup() {
        registerNodePeerFactory(Group.class, HtmlGroupPeer::new);
    }

    public static void registerRectangle() {
        registerNodePeerFactory(Rectangle.class, HtmlRectanglePeer::new);
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

    public static void registerRegion() {
    }

}
