package dev.webfx.kit.launcher;

import dev.webfx.kit.launcher.spi.FastPixelReaderWriter;
import dev.webfx.kit.launcher.spi.WebFxKitLauncherProvider;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.util.function.Factory;
import dev.webfx.platform.util.serviceloader.SingleServiceProvider;
import javafx.application.Application;
import javafx.geometry.Bounds;
import javafx.scene.image.Image;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebFxKitLauncher {

    public static synchronized WebFxKitLauncherProvider getProvider() {
        return SingleServiceProvider.getProvider(WebFxKitLauncherProvider.class, () -> ServiceLoader.load(WebFxKitLauncherProvider.class));
    }

    public static void launchApplication(Factory<Application> applicationFactory, String... args) {
        WebFxKitLauncherProvider provider = null;
        try {
            provider = getProvider();
        } catch (Exception e) {
            Console.log("No FxKitLauncher provider (assuming server side), JavaFX application will not be launched");
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

    public static Application getApplication() {
        return getProvider().getApplication();
    }

    public static Stage getPrimaryStage() {
        return getProvider().getPrimaryStage();
    }

    public static double getVerticalScrollbarExtraWidth() {
        return getProvider().getVerticalScrollbarExtraWidth();
    }

    public static boolean supportsWebP() {
        return getProvider().supportsWebP();
    }

    public static FastPixelReaderWriter getFastPixelReaderWriter(Image image) {
        return getProvider().getFastPixelReaderWriter(image);
    }

    public static Bounds measureText(String text, Font font) {
        return getProvider().measureText(text, font);
    }

}
