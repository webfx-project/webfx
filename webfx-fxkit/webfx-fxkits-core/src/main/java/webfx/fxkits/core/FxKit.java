package webfx.fxkits.core;

import javafx.application.Application;
import javafx.stage.Stage;
import webfx.fxkits.core.spi.FxKitProvider;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.util.function.Factory;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class FxKit {

    public static synchronized FxKitProvider getProvider() {
        return SingleServiceLoader.loadService(FxKitProvider.class);
    }

    public static String getUserAgent() {
        return getProvider().getUserAgent();
    }

    public static Stage getPrimaryStage() {
        return getProvider().getPrimaryStage();
    }

    public static void launchApplication(Factory<Application> applicationFactory, String... args) {
        FxKitProvider provider = null;
        try {
            provider = getProvider();
        } catch (Exception e) {
            Logger.log("No FxKit provider (assuming server side), JavaFx application will not be launched");
        }
        if (provider != null)
            provider.launchApplication(applicationFactory, args);
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
