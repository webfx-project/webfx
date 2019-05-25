package webfx.fxkit.javafxgraphics.mapper.highcoupling.spi;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.NodePeerFactoryRegistry;

/**
 * @author Bruno Salmon
 */
public interface FxKitMapperProvider {

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
