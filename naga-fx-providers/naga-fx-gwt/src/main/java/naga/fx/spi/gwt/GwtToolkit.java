package naga.fx.spi.gwt;

import elemental2.dom.DomGlobal;
import emul.javafx.geometry.Rectangle2D;
import emul.javafx.scene.Scene;
import emul.javafx.stage.Screen;
import emul.javafx.stage.Stage;
import emul.javafx.stage.Window;
import naga.scheduler.UiScheduler;
import naga.fx.spi.Toolkit;
import naga.fx.spi.gwt.html.HtmlScenePeer;
import naga.fx.spi.peer.ScenePeer;
import naga.fx.spi.peer.StagePeer;
import naga.fx.spi.peer.WindowPeer;
import naga.platform.spi.Platform;


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
        elemental2.dom.Screen screen = DomGlobal.screen;
        return Screen.from(toRectangle2D(screen.width, screen.height), toRectangle2D(screen.availWidth, screen.availHeight));
    }

    private static Rectangle2D toRectangle2D(double width, double height) {
        return new Rectangle2D(0, 0, width, height);
    }
}