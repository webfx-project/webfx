package webfx.platform.services.push.client.spi.impl;

import webfx.platform.bus.Registration;
import webfx.platform.bus.call.BusCallService;
import webfx.platform.services.log.Logger;
import webfx.platform.services.push.ClientPushBusAddressesSharedByBothClientAndServer;
import webfx.platform.services.push.client.spi.PushClientServiceProvider;

/**
 * @author Bruno Salmon
 */
public class PushClientServiceProviderImpl implements PushClientServiceProvider {

    @Override
    public Registration listenServerPushCalls(Object pushClientId) {
        String clientBusCallServiceAddress = ClientPushBusAddressesSharedByBothClientAndServer.computeClientBusCallServiceAddress(pushClientId);
        Logger.log("Subscribing " + clientBusCallServiceAddress);
        return BusCallService.listenBusEntryCalls(clientBusCallServiceAddress);
    }
}
