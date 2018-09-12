package webfx.platforms.core.services.query;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;

/**
 * @author Bruno Salmon
 */
public class QueryModule implements ApplicationModule {

    @Override
    public void start() {
        // Registering all required json codecs (especially for network bus calls)
        QueryService.registerJsonCodecsAndBusCalls();
    }
}
