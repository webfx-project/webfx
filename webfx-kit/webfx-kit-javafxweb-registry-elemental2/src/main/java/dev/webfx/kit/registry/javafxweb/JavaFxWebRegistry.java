package dev.webfx.kit.registry.javafxweb;

import dev.webfx.kit.mapper.peers.javafxweb.engine.WebEnginePeer;
import dev.webfx.kit.mapper.peers.javafxweb.spi.elemental2.HtmlWebViewPeer;
import javafx.scene.web.WebEngine;
import javafx.scene.web.WebView;

import static dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.registerNodePeerFactory;

/**
 * @author Bruno Salmon
 */
public class JavaFxWebRegistry {

    public static void registerWebView() {
        registerNodePeerFactory(WebView.class, HtmlWebViewPeer::new);
    }

    public static WebEnginePeer createWebEnginePeer(Object webEngine) {
        return new Elemental2WebEnginePeer((WebEngine) webEngine);
    }

}
