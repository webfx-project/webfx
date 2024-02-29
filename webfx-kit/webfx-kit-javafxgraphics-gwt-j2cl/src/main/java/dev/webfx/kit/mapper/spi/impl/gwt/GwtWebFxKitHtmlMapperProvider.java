package dev.webfx.kit.mapper.spi.impl.gwt;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlGraphicsContext;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.HtmlScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.ImageDataHelper;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.html.ImageDataPixelWriter;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.GwtJ2clPrimaryStagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.GwtJ2clSecondaryStagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwtj2cl.shared.GwtJ2clWindowPeer;
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
public final class GwtWebFxKitHtmlMapperProvider extends WebFxKitMapperProviderBase {

    @Override
    public StagePeer createStagePeer(Stage stage) {
        if (stage == WebFxKitLauncher.getPrimaryStage())
            return new GwtJ2clPrimaryStagePeer(stage);
        return new GwtJ2clSecondaryStagePeer(stage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return new GwtJ2clWindowPeer(window);
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
