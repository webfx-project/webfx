package dev.webfx.kit.registry.javafxweb;

import dev.webfx.kit.mapper.peers.javafxweb.spi.gwt.HtmlWebViewPeer;
import javafx.scene.web.WebView;

import static dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.registerNodePeerFactory;

/**
 * @author Bruno Salmon
 */
public class JavaFxWebRegistry {

    public static void registerWebView() {
        registerNodePeerFactory(WebView.class, HtmlWebViewPeer::new);
    }

}
