package webfx.platforms.core.services.bus.call;

import webfx.platforms.core.services.appcontainer.spi.ApplicationModule;

/**
 * @author Bruno Salmon
 */
public class BusCallModule implements ApplicationModule {

    @Override
    public void start() {
        BusCallService.registerJsonCodecs();
    }
}
