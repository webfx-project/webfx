package webfx.fxkit.gwt;

import javafx.scene.Scene;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.ScenePeer;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.StagePeer;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.WindowPeer;
import webfx.fxkit.javafxgraphics.mapper.highcoupling.spi.impl.FxKitMapperProviderBase;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.HtmlGraphicsContext;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.html.HtmlScenePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared.GwtPrimaryStagePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared.GwtSecondaryStagePeer;
import webfx.fxkit.javafxgraphics.mapper.spi.impl.peer.gwt.shared.GwtWindowPeer;
import webfx.fxkit.launcher.FxKitLauncher;

/**
 * @author Bruno Salmon
 */
public final class GwtFxKitHtmlMapperProvider extends FxKitMapperProviderBase {

    @Override
    public StagePeer createStagePeer(Stage stage) {
        if (stage == FxKitLauncher.getPrimaryStage())
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
}
