package dev.webfx.kit.mapper.spi.impl.elemental2;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlGraphicsContext;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.HtmlScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.ImageDataHelper;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.html.ImageDataPixelWriter;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.Elemental2PrimaryStagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.Elemental2SecondaryStagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.elemental2.shared.Elemental2WindowPeer;
import dev.webfx.kit.mapper.spi.impl.base.WebFxKitMapperProviderBase;
import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import javafx.scene.image.PixelWriter;
import javafx.stage.Stage;
import javafx.stage.Window;

/**
 * @author Bruno Salmon
 */
public final class Elemental2WebFxKitHtmlMapperProvider extends WebFxKitMapperProviderBase {

    @Override
    public StagePeer createStagePeer(Stage stage) {
        if (stage == WebFxKitLauncher.getPrimaryStage())
            return new Elemental2PrimaryStagePeer(stage);
        return new Elemental2SecondaryStagePeer(stage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return new Elemental2WindowPeer(window);
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return new HtmlScenePeer(scene);
    }

    @Override
    public GraphicsContext createGraphicsContext2D(Canvas canvas, boolean willReadFrequently) {
        return new HtmlGraphicsContext(canvas, willReadFrequently);
    }

    @Override
    public PixelWriter getImagePixelWriter(Image image) {
        return new ImageDataPixelWriter(image, ImageDataHelper.getOrCreateImageDataAssociatedWithImage(image));
    }
}
