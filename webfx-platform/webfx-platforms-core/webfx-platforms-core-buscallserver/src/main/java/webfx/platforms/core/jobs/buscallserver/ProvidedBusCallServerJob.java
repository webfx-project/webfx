package webfx.platforms.core.jobs.buscallserver;

/**
 * @author Bruno Salmon
 */

import webfx.platforms.core.services.appcontainer.spi.ApplicationJob;
import webfx.platforms.core.services.bus.Registrations;
import webfx.platforms.core.services.buscall.BusCallService;
import webfx.platforms.core.services.log.Logger;

public final class ProvidedBusCallServerJob implements ApplicationJob {

    private final Registrations registrations = new Registrations();

    @Override
    public void onStart() {
        // At this stage of initialization, the bus call end points should be already registered
        // So now starting the BusCallService by listening entry calls
        Logger.log("Starting listening bus entry calls");
        registrations.add(BusCallService.listenBusEntryCalls());
    }

    @Override
    public void onStop() {
        registrations.unregister();
    }
}
