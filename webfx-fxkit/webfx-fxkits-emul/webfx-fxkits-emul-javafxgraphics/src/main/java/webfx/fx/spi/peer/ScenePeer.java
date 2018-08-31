package webfx.fx.spi.peer;

import emul.com.sun.javafx.tk.TKScene;
import emul.javafx.collections.ListChangeListener;
import emul.javafx.scene.Node;
import emul.javafx.scene.Parent;
import emul.javafx.scene.Scene;

/**
 * @author Bruno Salmon
 */
public interface ScenePeer extends TKScene {

    Scene getScene();

    NodePeerFactory getNodePeerFactory();

    void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<Node> childrenChange);

    NodePeer pickPeer(double sceneX, double sceneY);

    default void onRootBound() {}

    default void onNodePeerCreated(NodePeer<Node> nodePeer) {}

    default void onPropertyHit() {}

    default void onBeforePulse() {}

    default void onAfterPulse() {}
}
