package naga.fx.spi.gwt;

import naga.commons.scheduler.UiScheduler;
import naga.fx.geometry.Rectangle2D;
import naga.fx.spi.peer.StagePeer;
import naga.fx.spi.peer.WindowPeer;
import naga.fx.scene.Scene;
import naga.fx.spi.peer.ScenePeer;
import naga.fx.spi.Toolkit;
import naga.fx.spi.gwt.html.HtmlScenePeer;
import naga.fx.stage.*;
import naga.platform.spi.Platform;

import static elemental2.Global.window;

/**
 * @author Bruno Salmon
 */
public class GwtToolkit extends Toolkit {

    public GwtToolkit() {
        super(/* TODO: remove this dependency to Platform */(UiScheduler) Platform.get().scheduler());
    }

    @Override
    public StagePeer createStagePeer(Stage stage) {
        if (stage == getPrimaryStage())
            return new GwtPrimaryStagePeer(stage);
        return new GwtSecondaryStagePeer(stage);
    }

    @Override
    public WindowPeer createWindowPeer(Window window) {
        return null;
    }

    @Override
    public ScenePeer createScenePeer(Scene scene) {
        return new HtmlScenePeer(scene);
    }

    @Override
    public Screen getPrimaryScreen() {
        elemental2.Screen screen = window.screen;
        return Screen.from(toRectangle2D(screen.width, screen.height), toRectangle2D(screen.availWidth, screen.availHeight));
    }

    private static Rectangle2D toRectangle2D(double width, double height) {
        return new Rectangle2D(0, 0, width, height);
    }
}