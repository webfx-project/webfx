package dev.webfx.kit.launcher;

import dev.webfx.platform.boot.ApplicationBooter;
import dev.webfx.platform.boot.spi.ApplicationModuleBooter;
import javafx.application.Application;
import dev.webfx.platform.service.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebFxKitLauncherModuleBooter implements ApplicationModuleBooter {

    @Override
    public String getModuleName() {
        return "webfx-kit-launcher";
    }

    @Override
    public int getBootLevel() {
        return APPLICATION_LAUNCH_LEVEL;
    }

    @Override
    public void bootModule() {
        WebFxKitLauncher.launchApplication(() -> SingleServiceProvider.getProvider(Application.class, () -> ServiceLoader.load(Application.class), SingleServiceProvider.NotFoundPolicy.RETURN_NULL), ApplicationBooter.getMainArgs());
    }

    @Override
    public void exitModule() {

    }
}
