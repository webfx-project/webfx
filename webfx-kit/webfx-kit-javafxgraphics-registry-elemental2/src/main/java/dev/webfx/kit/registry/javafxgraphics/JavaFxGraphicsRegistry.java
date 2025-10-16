package dev.webfx.kit.registry.javafxgraphics;

import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.*;
import javafx.scene.canvas.Canvas;
import javafx.scene.image.ImageView;
import javafx.scene.shape.*;
import javafx.scene.text.Text;

import static dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.*;


public class JavaFxGraphicsRegistry {

    public static void registerGroup() {
        NodePeerFactoryRegistry.registerDefaultGroupPeerFactory(group -> {
            String tag = requestedCustomTag(group);
            if (tag == null) {
                String classTag = classTag(group);
                // Hot registration for future cases
                registerNodePeerFactory(group.getClass(), () -> new HtmlGroupPeer<>(classTag));
                tag = classTag;
            }
            return new HtmlGroupPeer<>(tag);
        });
    }

    public static void registerRegion() {
        NodePeerFactoryRegistry.registerDefaultRegionPeerFactory(region -> {
            String tag = requestedCustomTag(region);
            if (tag != null) { // Ex: HTML "div"
                // In this case, we return a HtmlBrowserRegionPeer instance, which is not supposed to have children
                // managed by JavaFX, and its size (prefHeight(), getLayoutBounds(), etc...) will actually be computed
                // by the browser and not JavaFX. Its main usage should be to act as a container for a third-party
                // component (ex: a JS library like Google Map or any other) that will be integrated seamlessly in the
                // browser page (without the need of an iFrame, i.e. a WebView in JavaFX). Usually the application code
                // should then set an id on it - using setId() - and pass that id to the JS library.
                return new HtmlBrowserRegionPeer<>(tag);
            }
            // For other cases, we return a HtmlJavaFXRegionPeer instance, which will map all other classes extending
            // the JavaFX region class, either from OpenJFX (such as Pane, VBox, etc...) or from the application code
            // which can also extends Region or its subclasses. In this case, WebFX will map its children to the DOM.
            // The layout of these children will be managed by the class itself (via the layoutChildren() method).
            String classTag = classTag(region);
            // Hot registration of the extended region class for future cases (will be a bit faster)
            registerNodePeerFactory(region.getClass(), () -> new HtmlJavaFXRegionPeer<>(classTag));
            // But for this first call with this specific class, we instantiate the peer now.
            return new HtmlJavaFXRegionPeer<>(classTag);
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

    public static void registerSubtractShape() {
        registerNodePeerFactory(SubtractShape.class, HtmlSubtractShapePeer::new);
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

}
