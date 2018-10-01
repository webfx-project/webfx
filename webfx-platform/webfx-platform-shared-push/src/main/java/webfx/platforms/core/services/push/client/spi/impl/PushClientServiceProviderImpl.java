package webfx.platforms.core.services.push.client.spi.impl;

import webfx.platforms.core.services.bus.Registration;
import webfx.platforms.core.services.buscall.BusCallService;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.push.ClientPushBusAddressesSharedByBothClientAndServer;
import webfx.platforms.core.services.push.client.spi.PushClientServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class PushClientServiceProviderImpl implements PushClientServiceProvider {

    @Override
    public Registration listenServerPushCalls(Object pushClientId) {
        String clientBusCallServiceAddress = ClientPushBusAddressesSharedByBothClientAndServer.computeClientBusCallServiceAddress(pushClientId);
        Logger.log("Subscribing " + clientBusCallServiceAddress);
        return BusCallService.listenBusEntryCalls(clientBusCallServiceAddress);
    }
}
