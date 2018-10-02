package webfx.platform.client.services.push.spi.impl.simple;

import webfx.platform.client.services.push.spi.PushClientServiceProvider;
import webfx.platform.shared.services.bus.Registration;
import webfx.platform.shared.services.buscall.BusCallService;
import webfx.platform.shared.services.log.Logger;
import webfx.platform.shared.services.push.ClientPushBusAddressesSharedByBothClientAndServer;

/**
 * @author Bruno Salmon
 */
public final class SimplePushClientServiceProvider implements PushClientServiceProvider {

    @Override
    public Registration listenServerPushCalls(Object pushClientId) {
        String clientBusCallServiceAddress = ClientPushBusAddressesSharedByBothClientAndServer.computeClientBusCallServiceAddress(pushClientId);
        Logger.log("Subscribing " + clientBusCallServiceAddress);
        return BusCallService.listenBusEntryCalls(clientBusCallServiceAddress);
    }
}
