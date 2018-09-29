package webfx.fxkits.core.mapper;

import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkits.core.mapper.spi.NodePeer;
import webfx.fxkits.core.mapper.spi.ScenePeer;
import webfx.fxkits.core.mapper.spi.StagePeer;
import webfx.fxkits.core.mapper.spi.WindowPeer;
import webfx.fxkits.core.mapper.spi.FxKitMapperProvider;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class FxKitMapper {

    public static FxKitMapperProvider getProvider() {
        return SingleServiceLoader.loadService(FxKitMapperProvider.class);
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
        return getProvider().createNodePeer(node);
    }

    public static  double getVerticalScrollbarExtraWidth() {
        return getProvider().getVerticalScrollbarExtraWidth();
    }
}
