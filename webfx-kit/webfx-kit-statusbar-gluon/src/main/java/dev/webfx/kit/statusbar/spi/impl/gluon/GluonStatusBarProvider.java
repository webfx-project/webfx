package dev.webfx.kit.statusbar.spi.impl.gluon;

import dev.webfx.kit.statusbar.spi.StatusBarProvider;
import dev.webfx.platform.console.Console;
import dev.webfx.platform.uischeduler.UiScheduler;
import javafx.scene.paint.Color;

/**
 * @author Bruno Salmon
 */
public final class GluonStatusBarProvider implements StatusBarProvider {

    private com.gluonhq.attach.statusbar.StatusBarService statusBarService;

    public GluonStatusBarProvider() {
        // Note: All Gluon servies must be created in the UI thread, otherwise the application crashes
        UiScheduler.runInUiThread(() -> {
            statusBarService = com.gluonhq.attach.statusbar.StatusBarService.create().orElse(null);
            if (statusBarService == null) // It seems the audio service is implemented only for Android, so this happens on other platforms
                Console.log("WARNING [WebFX Platform]: Unable to load Gluon StatusBar Service");
            else
                Console.log("Gluon StatusBar Service created");
        });
    }


    @Override
    public boolean hasStatusBar() {
        return statusBarService != null;
    }

    @Override
    public boolean setColor(Color color) {
        if (statusBarService == null) {
            Console.log("WARNING [WebFX Kit]: StatusBar.setColor() called but Gluon StatusBar Service is null");
            return false;
        }
        statusBarService.setColor(color);
        Console.log("INFO [WebFX Kit]: StatusBar.setColor() successfully called with color = " + color);
        return true;
    }
}
