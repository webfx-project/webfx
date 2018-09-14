package webfx.platforms.core.services.querypush;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public class QueryPushModuleInitializer implements ApplicationModuleInitializer {

    @Override
    public String getModuleName() {
        return "webfx-platforms-core-querypush";
    }

    @Override
    public void initModule() {
        // Registering all required json codecs (especially for network bus calls)
        QueryPushService.registerJsonCodecsAndBusCalls();
    }
}
