package dev.webfx.kit.mapper.spi;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.Window;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry;

/**
 * @author Bruno Salmon
 */
public interface WebFxKitMapperProvider {

    StagePeer createStagePeer(Stage stage);

    WindowPeer createWindowPeer(Window window);

    ScenePeer createScenePeer(Scene scene);

    default <N extends Node, V extends NodePeer<N>> V createNodePeer(N node) {
        return NodePeerFactoryRegistry.createNodePeer(node);
    }

    default GraphicsContext getGraphicsContext2D(Canvas canvas) {
        return null;
    }

}
