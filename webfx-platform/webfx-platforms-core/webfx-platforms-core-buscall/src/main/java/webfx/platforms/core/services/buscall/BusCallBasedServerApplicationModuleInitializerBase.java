package webfx.platforms.core.services.buscall;

import webfx.platforms.core.services.appcontainer.ApplicationContainer;
import webfx.platforms.core.services.appcontainer.spi.ApplicationModuleInitializer;

/**
 * @author Bruno Salmon
 */
public abstract class BusCallBasedServerApplicationModuleInitializerBase implements ApplicationModuleInitializer {

    @Override
    public int getInitLevel() {
        return BUS_START_INIT_LEVEL;
    }

    @Override
    public void initModule() {
        startListeningBusCalls();
    }

    protected void startListeningBusCalls() {
        // Starting the server module that listen the bus calls so clients can communicate with the server
        ApplicationContainer.startApplicationService(new BusCallServerService());
    }
}
