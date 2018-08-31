package webfx.fx.spi;

import javafx.stage.Screen;
import javafx.stage.Stage;
import webfx.platform.services.uischeduler.spi.UiSchedulerProvider;
import webfx.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public abstract class Toolkit {

    private final UiSchedulerProvider uiSchedulerProvider;
    private Stage primaryStage;

    public Toolkit(UiSchedulerProvider uiSchedulerProvider) {
        this(uiSchedulerProvider, null);
    }

    public Toolkit(UiSchedulerProvider uiSchedulerProvider, Stage primaryStage) {
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

    private static Toolkit TOOLKIT;

    public static synchronized Toolkit get() {
        if (TOOLKIT == null) {
            //Platform.log("Getting toolkit");
            register(ServiceLoaderHelper.loadService(Toolkit.class));
            //Platform.log("Toolkit ok");
        }
        return TOOLKIT;
    }

    public static synchronized void register(Toolkit toolkit) {
        TOOLKIT = toolkit;
    }

}
