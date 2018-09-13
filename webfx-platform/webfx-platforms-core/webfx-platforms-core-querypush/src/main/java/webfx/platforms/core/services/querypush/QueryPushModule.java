package webfx.platforms.core.services.querypush;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;

/**
 * @author Bruno Salmon
 */
public class QueryPushModule implements ApplicationModule {

    @Override
    public void start() {
        // Registering all required json codecs (especially for network bus calls)
        QueryPushService.registerJsonCodecsAndBusCalls();
    }
}
