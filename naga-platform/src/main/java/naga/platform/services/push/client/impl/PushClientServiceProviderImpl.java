package naga.platform.services.push.client.impl;

import naga.platform.bus.Registration;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.push.DefaultClientBusCallAddressComputer;
import naga.platform.services.push.client.spi.PushClientServiceProvider;

/**
 * @author Bruno Salmon
 */
public class PushClientServiceProviderImpl implements PushClientServiceProvider {

    @Override
    public Registration listenServerPushCalls(Object pushClientId) {
        String clientBusCallServiceAddress = DefaultClientBusCallAddressComputer.computeClientBusCallServiceAddress(pushClientId);
        Logger.log("Subscribing " + clientBusCallServiceAddress);
        return BusCallService.listenBusEntryCalls(clientBusCallServiceAddress);
    }
}
