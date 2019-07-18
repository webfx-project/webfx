package webfx.fxkit.launcher.spi;

import javafx.application.Application;
import javafx.application.HostServices;
import javafx.scene.input.Clipboard;
import javafx.stage.Screen;
import javafx.stage.Stage;
import webfx.platform.client.services.uischeduler.UiScheduler;
import webfx.platform.shared.util.function.Factory;

/**
 * @author Bruno Salmon
 */
public interface FxKitLauncherProvider {

    String getUserAgent();

    HostServices getHostServices();

    boolean isStageProgrammaticallyRelocatableAndResizable();

    default Screen getPrimaryScreen() {
        return Screen.getPrimary();
    }

    default Clipboard getSystemClipboard() {
        return Clipboard.getSystemClipboard();
    }

    Stage getPrimaryStage();

    Application getApplication();

    void launchApplication(Factory<Application> applicationFactory, String... args);

    default boolean isReady() {
        return true;
    }

    default void onReady(Runnable runnable) {
        UiScheduler.runInUiThread(runnable);
    }

    default double getVerticalScrollbarExtraWidth() {
        return 0;
    }
}
