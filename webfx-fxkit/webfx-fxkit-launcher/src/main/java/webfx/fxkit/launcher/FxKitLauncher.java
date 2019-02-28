package webfx.fxkit.launcher;

import javafx.application.Application;
import javafx.stage.Stage;
import webfx.fxkit.launcher.spi.FxKitLauncherProvider;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.util.function.Factory;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class FxKitLauncher {

    public static synchronized FxKitLauncherProvider getProvider() {
        return SingleServiceProvider.getProvider(FxKitLauncherProvider.class, () -> ServiceLoader.load(FxKitLauncherProvider.class));
    }

    public static String getUserAgent() {
        return getProvider().getUserAgent();
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

    public static  double getVerticalScrollbarExtraWidth() {
        return getProvider().getVerticalScrollbarExtraWidth();
    }

}
