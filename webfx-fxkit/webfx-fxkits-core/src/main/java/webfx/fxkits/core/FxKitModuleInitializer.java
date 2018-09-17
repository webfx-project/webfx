package webfx.fxkits.core;

import javafx.application.Application;
import webfx.platforms.core.services.appcontainer.ApplicationContainer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class FxKitModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-fxkits-core";
    }

    @Override
    public int getInitLevel() {
        return FXKIT_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        FxKit.launchApplication(() -> SingleServiceLoader.loadService(Application.class, SingleServiceLoader.NotFoundPolicy.TRACE_AND_RETURN_NULL), ApplicationContainer.getMainArgs());
    }

    @Override
    public void exitModule() {

    }
}
