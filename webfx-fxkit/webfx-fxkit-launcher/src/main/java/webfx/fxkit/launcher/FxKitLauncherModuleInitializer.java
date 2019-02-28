package webfx.fxkit.launcher;

import javafx.application.Application;
import webfx.platform.shared.services.appcontainer.ApplicationContainer;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class FxKitLauncherModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-fxkit-launcher";
    }

    @Override
    public int getInitLevel() {
        return APPLICATION_LAUNCH_LEVEL;
    }

    @Override
    public void initModule() {
        FxKitLauncher.launchApplication(() ->  SingleServiceProvider.getProvider(Application.class, () -> ServiceLoader.load(Application.class), SingleServiceProvider.NotFoundPolicy.RETURN_NULL), ApplicationContainer.getMainArgs());
    }

    @Override
    public void exitModule() {

    }
}
