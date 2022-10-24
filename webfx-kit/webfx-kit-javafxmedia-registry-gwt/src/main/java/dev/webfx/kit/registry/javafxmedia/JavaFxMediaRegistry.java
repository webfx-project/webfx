package dev.webfx.kit.registry.javafxmedia;

import javafx.scene.media.MediaView;

import static dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry.registerNodePeerFactory;
/**
 * @author Bruno Salmon
 */
public class JavaFxMediaRegistry {

    public static void registerMediaView() {
        registerNodePeerFactory(MediaView.class, dev.webfx.kit.mapper.peers.javafxmedia.spi.gwt.HtmlMediaViewPeer::new);
    }

}
