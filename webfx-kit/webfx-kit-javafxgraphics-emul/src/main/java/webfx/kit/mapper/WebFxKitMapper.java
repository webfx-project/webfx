package webfx.kit.mapper;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import webfx.kit.mapper.spi.WebFxKitMapperProvider;
import webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebFxKitMapper {

    private static WebFxKitMapperProvider getProvider() {
        return SingleServiceProvider.getProvider(WebFxKitMapperProvider.class, () -> ServiceLoader.load(WebFxKitMapperProvider.class));
    }

    public static StagePeer createStagePeer(Stage stage) {
        return getProvider().createStagePeer(stage);
    }

    public static WindowPeer createWindowPeer(Window window) {
        return getProvider().createWindowPeer(window);
    }

    public static ScenePeer createScenePeer(Scene scene) {
        return getProvider().createScenePeer(scene);
    }

    public static <N extends Node, V extends NodePeer<N>> V createNodePeer(N node) {
        return NodePeerFactoryRegistry.createNodePeer(node);
    }

    public static GraphicsContext getGraphicsContext2D(Canvas canvas) {
        return getProvider().getGraphicsContext2D(canvas);
    }
}
