package webfx.platforms.core.services.buscall;

/**
 * @author Bruno Salmon
 */

import webfx.platforms.core.services.appcontainer.ApplicationJob;
import webfx.platforms.core.util.async.Future;

public final class BusCallServerJob implements ApplicationJob {

    public static final String VERSION_ADDRESS = "version";

    public String getVersion() {
        return "Webfx version 0.1.0-SNAPSHOT";
    }

    @Override
    public Future<Void> onStart() {
        // Registering java services so they can be called through the BusCallService
        BusCallService.registerJavaCallableAsCallableService(VERSION_ADDRESS, this::getVersion);
        // Other services should be already registered by application initializers

        // Starting the BusCallService by listening entry calls
        BusCallService.listenBusEntryCalls();
        return Future.succeededFuture();
    }
}
