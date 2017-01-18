package naga.fx.spi.peer;

import com.sun.javafx.tk.TKScene;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;

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
