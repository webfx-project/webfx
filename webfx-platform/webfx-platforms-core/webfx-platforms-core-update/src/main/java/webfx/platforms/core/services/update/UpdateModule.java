package webfx.platforms.core.services.update;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;

/**
 * @author Bruno Salmon
 */
public class UpdateModule implements ApplicationModule {

    @Override
    public void start() {
        // Registering all required json codecs (especially for network bus calls)
        UpdateService.registerJsonCodecsAndBusCalls();
    }
}
