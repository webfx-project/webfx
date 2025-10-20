package dev.webfx.kit.mapper.spi.impl.elemental2;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.spi.impl.base.WebFxKitMapperProviderBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.Elemental2PrimaryStagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.Elemental2SecondaryStagePeer;
import dev.webfx.kit.launcher.WebFxKitLauncher;

/**
 * @author Bruno Salmon
 */
public final class Elemental2WebFxKitSvgMapperProvider extends WebFxKitMapperProviderBase {

    @Override
    public StagePeer createStagePeer(Stage stage) {
        if (stage == WebFxKitLauncher.getPrimaryStage())
            return new Elemental2PrimaryStagePeer(stage);
        return new Elemental2SecondaryStagePeer(stage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return null;
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return new HtmlScenePeer(scene);
    }
}
