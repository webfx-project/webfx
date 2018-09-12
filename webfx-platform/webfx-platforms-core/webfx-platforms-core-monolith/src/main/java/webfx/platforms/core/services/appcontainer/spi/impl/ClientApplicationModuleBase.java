package webfx.platforms.core.services.appcontainer.spi.impl;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;
import webfx.platforms.core.services.bus.spi.BusService;

/**
 * @author Bruno Salmon
 */
public class ClientApplicationModuleBase implements ApplicationModule {


    @Override
    public void start() {
        // Initializing the bus immediately to make the connection connection process happen while the application is initializing
        initializeBusConnection();
    }

    protected void initializeBusConnection() {
        BusService.bus(); // Instantiating the bus (if not already done) is enough to open the connection
    }
}
