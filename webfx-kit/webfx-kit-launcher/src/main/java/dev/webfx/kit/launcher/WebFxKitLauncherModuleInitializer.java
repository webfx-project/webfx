package dev.webfx.kit.launcher;

import javafx.application.Application;
import dev.webfx.platform.shared.services.appcontainer.ApplicationContainer;
import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class WebFxKitLauncherModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-kit-launcher";
    }

    @Override
    public int getInitLevel() {
        return APPLICATION_LAUNCH_LEVEL;
    }

    @Override
    public void initModule() {
        WebFxKitLauncher.launchApplication(() ->  SingleServiceProvider.getProvider(Application.class, () -> ServiceLoader.load(Application.class), SingleServiceProvider.NotFoundPolicy.RETURN_NULL), ApplicationContainer.getMainArgs());
    }

    @Override
    public void exitModule() {

    }
}
