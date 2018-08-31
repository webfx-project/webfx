package webfx.fx.spi.peer;

import emul.javafx.scene.Node;

/**
 * @author Bruno Salmon
 */
public interface NodePeerFactory {

    <N extends Node, V extends NodePeer<N>> V createNodePeer(N node);

}
