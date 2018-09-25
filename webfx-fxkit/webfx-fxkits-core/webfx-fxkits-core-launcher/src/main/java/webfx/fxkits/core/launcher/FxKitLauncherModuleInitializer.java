package webfx.fxkits.core.launcher;

import javafx.application.Application;
import webfx.platforms.core.services.appcontainer.ApplicationContainer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class FxKitLauncherModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-fxkits-core-launcher";
    }

    @Override
    public int getInitLevel() {
        return APPLICATION_LAUNCH_LEVEL;
    }

    @Override
    public void initModule() {
        FxKitLauncher.launchApplication(() -> SingleServiceLoader.loadService(Application.class, SingleServiceLoader.NotFoundPolicy.TRACE_AND_RETURN_NULL), ApplicationContainer.getMainArgs());
    }

    @Override
    public void exitModule() {

    }
}
