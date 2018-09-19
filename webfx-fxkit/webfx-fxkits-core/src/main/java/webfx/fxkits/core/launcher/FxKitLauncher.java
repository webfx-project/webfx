package webfx.fxkits.core.launcher;

import javafx.application.Application;
import javafx.stage.Screen;
import javafx.stage.Stage;
import webfx.fxkits.core.launcher.spi.FxKitLauncherProvider;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.util.function.Factory;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class FxKitLauncher {

    public static synchronized FxKitLauncherProvider getProvider() {
        return SingleServiceLoader.loadService(FxKitLauncherProvider.class);
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

    public static void launchApplication(Factory<Application> applicationFactory, String... args) {
        FxKitLauncherProvider provider = null;
        try {
            provider = getProvider();
        } catch (Exception e) {
            Logger.log("No FxKitLauncher provider (assuming server side), JavaFx application will not be launched");
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

}
