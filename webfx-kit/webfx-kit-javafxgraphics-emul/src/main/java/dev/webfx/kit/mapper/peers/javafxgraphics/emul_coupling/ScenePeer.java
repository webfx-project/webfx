package dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling;

import com.sun.javafx.tk.TKScene;
import javafx.collections.ListChangeListener;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;

/**
 * @author Bruno Salmon
 */
public interface ScenePeer extends TKScene {

    Scene getScene();

    void updateParentAndChildrenPeers(Parent parent, ListChangeListener.Change<? extends Node> childrenChange);

    NodePeer pickPeer(double sceneX, double sceneY);

    default void onRootBound() {}

    default void onNodePeerCreated(NodePeer<Node> nodePeer) {}

    default void onPropertyHit() {}

    default void onBeforePulse() {}

    default void onAfterPulse() {}
}
