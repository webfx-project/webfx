package webfx.platforms.core.services.bus.call;

import webfx.platforms.core.services.appcontainer.ApplicationContainer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;

/**
 * @author Bruno Salmon
 */
public class BusCallBasedServerApplicationModuleBase implements ApplicationModule {

    @Override
    public void start() {
        startListeningBusCalls();
    }

    protected void startListeningBusCalls() {
        // Starting the server module that listen the bus calls so clients can communicate with the server
        ApplicationContainer.startApplicationService(new BusCallServerService());
    }
}
