package dev.webfx.kit.mapper.spi.impl.gwt;

import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.Window;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.spi.impl.base.WebFxKitMapperProviderBase;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.GwtJ2clPrimaryStagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.GwtJ2clSecondaryStagePeer;
import dev.webfx.kit.launcher.WebFxKitLauncher;

/**
 * @author Bruno Salmon
 */
public final class GwtWebFxKitSvgMapperProvider extends WebFxKitMapperProviderBase {

    @Override
    public StagePeer createStagePeer(Stage stage) {
        if (stage == WebFxKitLauncher.getPrimaryStage())
            return new GwtJ2clPrimaryStagePeer(stage);
        return new GwtJ2clSecondaryStagePeer(stage);
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
