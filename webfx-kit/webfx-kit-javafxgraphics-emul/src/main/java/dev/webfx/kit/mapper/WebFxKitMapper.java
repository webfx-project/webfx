package dev.webfx.kit.mapper;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.Window;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.spi.WebFxKitMapperProvider;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.NodePeerFactoryRegistry;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

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
