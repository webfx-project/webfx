package dev.webfx.kit.mapper.spi.impl.gwt;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.ScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.StagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.emul_coupling.WindowPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlCanvasPeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlGraphicsContext;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.HtmlScenePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.html.ImageDataPixelWriter;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.GwtPrimaryStagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.GwtSecondaryStagePeer;
import dev.webfx.kit.mapper.peers.javafxgraphics.gwt.shared.GwtWindowPeer;
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
            return new GwtPrimaryStagePeer(stage);
        return new GwtSecondaryStagePeer(stage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return new GwtWindowPeer(window);
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return new HtmlScenePeer(scene);
    }

    @Override
    public GraphicsContext getGraphicsContext2D(Canvas canvas) {
        return new HtmlGraphicsContext(canvas);
    }

    @Override
    public PixelWriter getImagePixelWriter(Image image) {
        return new ImageDataPixelWriter(
                HtmlCanvasPeer.getCanvasImageData(
                        HtmlCanvasPeer.createCanvasElement((int) image.getWidth(), (int) image.getHeight())));
    }
}
