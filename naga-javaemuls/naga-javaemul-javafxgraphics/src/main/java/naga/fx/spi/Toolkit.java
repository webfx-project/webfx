package naga.fx.spi;

import emul.javafx.scene.Scene;
import emul.javafx.stage.Screen;
import emul.javafx.stage.Stage;
import emul.javafx.stage.Window;
import naga.platform.services.uischeduler.spi.UiSchedulerProvider;
import naga.util.serviceloader.ServiceLoaderHelper;
import naga.fx.spi.peer.ScenePeer;
import naga.fx.spi.peer.StagePeer;
import naga.fx.spi.peer.WindowPeer;

/**
 * @author Bruno Salmon
 */
public abstract class Toolkit {

    private final UiSchedulerProvider uiSchedulerProvider;
    private Stage primaryStage;

    public Toolkit(UiSchedulerProvider uiSchedulerProvider) {
        this.uiSchedulerProvider = uiSchedulerProvider;
    }

    public abstract String getUserAgent();

    public abstract Screen getPrimaryScreen();

    public Stage getPrimaryStage() {
        if (primaryStage == null) {
            primaryStage = new Stage();
            primaryStage.impl_setPrimary(true);
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
        get().scheduler().runInUiThread(runnable);
    }

    public UiSchedulerProvider scheduler() {
        return uiSchedulerProvider;
    }

    public static boolean isUiThread() {
        return get().scheduler().isUiThread();
    }

    public double getVerticalScrollbarExtraWidth() {
        return 16;
    }

    private static Toolkit TOOLKIT;

    public static synchronized Toolkit get() {
        if (TOOLKIT == null) {
            //Platform.log("Getting toolkit");
            TOOLKIT = ServiceLoaderHelper.loadService(Toolkit.class);
            //Platform.log("Toolkit ok");
        }
        return TOOLKIT;
    }

}
