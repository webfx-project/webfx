package webfx.fxkits.core.spi;

import javafx.scene.Scene;
import javafx.stage.Screen;
import javafx.stage.Stage;
import javafx.stage.Window;
import webfx.fxkits.core.spi.peer.ScenePeer;
import webfx.fxkits.core.spi.peer.StagePeer;
import webfx.fxkits.core.spi.peer.WindowPeer;
import webfx.platforms.core.services.uischeduler.spi.UiSchedulerProvider;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public abstract class FxKit {

    private final UiSchedulerProvider uiSchedulerProvider;
    private Stage primaryStage;

    public FxKit(UiSchedulerProvider uiSchedulerProvider) {
        this(uiSchedulerProvider, null);
    }

    public FxKit(UiSchedulerProvider uiSchedulerProvider, Stage primaryStage) {
        this.uiSchedulerProvider = uiSchedulerProvider;
        this.primaryStage = primaryStage;
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

    private static FxKit FXKIT;

    public static synchronized FxKit get() {
        if (FXKIT == null) {
            //Platform.log("Getting FxKit");
            FXKIT = ServiceLoaderHelper.loadService(FxKit.class);
            //Platform.log("FxKit ok");
        }
        return FXKIT;
    }

}
