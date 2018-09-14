package webfx.fxkits.core;

import javafx.stage.Screen;
import javafx.stage.Stage;
import webfx.fxkits.core.spi.FxKitProvider;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class FxKit {

    private static FxKitProvider FXKIT;

    public static synchronized FxKitProvider getProvider() {
        if (FXKIT == null)
            FXKIT = SingleServiceLoader.loadService(FxKitProvider.class);
        return FXKIT;
    }

    public static String getUserAgent() {
        return getProvider().getUserAgent();
    }

    public static Screen getPrimaryScreen() {
        return getProvider().getPrimaryScreen();
    }

    public static Stage getPrimaryStage() {
        return getProvider().getPrimaryStage();
    }

    public static boolean isReady() {
        return getProvider().isReady();
    }

    public static void onReady(Runnable runnable) {
        getProvider().onReady(runnable);
    }

    public static double getVerticalScrollbarExtraWidth() {
        return getProvider().getVerticalScrollbarExtraWidth();
    }

}
