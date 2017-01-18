package naga.fx.spi.peer;

import emul.javafx.scene.Node;
import emul.javafx.scene.Parent;
import emul.javafx.scene.Scene;
import emul.com.sun.javafx.tk.TKScene;

/**
 * @author Bruno Salmon
 */
public interface ScenePeer extends TKScene {

    Scene getScene();

    NodePeerFactory getNodePeerFactory();

    void updateParentAndChildrenPeers(Parent parent);

    default void onRootBound() {}

    default void onNodePeerCreated(NodePeer<Node> nodePeer) {}

    default void onPropertyHit() {}

    default void onBeforePulse() {}

    default void onAfterPulse() {}
}
