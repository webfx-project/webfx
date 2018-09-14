package webfx.fxkits.core.spi;

import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkits.core.spi.peer.ScenePeer;
import webfx.fxkits.core.spi.peer.StagePeer;
import webfx.fxkits.core.spi.peer.WindowPeer;
import webfx.platforms.core.services.uischeduler.UiScheduler;

/**
 * @author Bruno Salmon
 */
public abstract class FxKitProvider {

    private Stage primaryStage;

    public FxKitProvider() {
        this(null);
    }

    public FxKitProvider(Stage primaryStage) {
        this.primaryStage = primaryStage;
    }

    public abstract String getUserAgent();

    public abstract Screen getPrimaryScreen();

    public Stage getPrimaryStage() {
        if (primaryStage == null) {
            primaryStage = new Stage();
            //primaryStage.impl_setPrimary(true);
        }
        return primaryStage;
    }

    public abstract StagePeer createStagePeer(Stage stage);

    public abstract WindowPeer createWindowPeer(Window window);

    public abstract ScenePeer createScenePeer(Scene scene);

    public boolean isReady() {
        return true;
    }

    public void onReady(Runnable runnable) {
        UiScheduler.runInUiThread(runnable);
    }

    public double getVerticalScrollbarExtraWidth() {
        return 16;
    }

}
