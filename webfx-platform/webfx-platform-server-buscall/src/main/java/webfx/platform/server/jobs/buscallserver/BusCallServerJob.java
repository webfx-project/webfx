package webfx.platform.server.jobs.buscallserver;

/**
 * @author Bruno Salmon
 */

import webfx.platform.shared.services.appcontainer.spi.ApplicationJob;
import webfx.platform.shared.services.bus.Registrations;
import webfx.platform.shared.services.buscall.BusCallService;
import webfx.platform.shared.services.log.Logger;

public final class BusCallServerJob implements ApplicationJob {

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
