package webfx.fxkits.core.launcher;

import javafx.application.Application;
import webfx.platform.shared.services.appcontainer.ApplicationContainer;
import webfx.platform.shared.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platform.shared.util.serviceloader.SingleServiceLoader;

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
