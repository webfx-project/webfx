package webfx.fxkits.core.spi;

import javafx.stage.Screen;
import javafx.stage.Stage;
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
            //primaryStage.impl_setPrimary(true); // Not accessible anymore in Java 9
        }
        return primaryStage;
    }

/*
    public abstract StagePeer createStagePeer(Stage stage);

    public abstract WindowPeer createWindowPeer(Window window);

    public abstract ScenePeer createScenePeer(Scene scene);
*/

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
            register(ServiceLoaderHelper.loadService(FxKit.class));
            //Platform.log("FxKit ok");
        }
        return FXKIT;
    }

    public static synchronized void register(FxKit fxKit) {
        FXKIT = fxKit;
    }

}
