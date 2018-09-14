package webfx.platforms.core.services.query;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public class QueryModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platforms-core-query";
    }

    @Override
    public void initModule() {
        // Registering all required json codecs (especially for network bus calls)
        QueryService.registerJsonCodecsAndBusCalls();
    }
}
