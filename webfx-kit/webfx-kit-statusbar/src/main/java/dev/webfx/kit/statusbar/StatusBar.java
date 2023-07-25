package dev.webfx.kit.statusbar;

import dev.webfx.kit.statusbar.spi.StatusBarProvider;
import dev.webfx.platform.util.serviceloader.SingleServiceProvider;
import javafx.scene.paint.Color;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class StatusBar {

    public static StatusBarProvider getProvider() {
        return SingleServiceProvider.getProvider(StatusBarProvider.class, () -> ServiceLoader.load(StatusBarProvider.class));
    }

    public static boolean hasStatusBar() {
        return getProvider().hasStatusBar();
    }

    public static boolean setColor(Color color) {
        return getProvider().setColor(color);
    }

}
