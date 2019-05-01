package webfx.fxkit.mapper.spi;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Bruno Salmon
 */
public interface FxKitMapperProvider {

    StagePeer createStagePeer(Stage stage);

    WindowPeer createWindowPeer(Window window);

    ScenePeer createScenePeer(Scene scene);

    <N extends Node, V extends NodePeer<N>> V createNodePeer(N node);

    default GraphicsContext getGraphicsContext2D(Canvas canvas) {
        return null;
    }

}
