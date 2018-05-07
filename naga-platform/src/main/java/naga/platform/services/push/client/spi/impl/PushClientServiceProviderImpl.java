package naga.platform.services.push.client.spi.impl;

import naga.platform.bus.Registration;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.log.Logger;
import naga.platform.services.push.SharedClientServerPushInfo;
import naga.platform.services.push.client.spi.PushClientServiceProvider;

/**
 * @author Bruno Salmon
 */
public class PushClientServiceProviderImpl implements PushClientServiceProvider {

    @Override
    public Registration listenServerPushCalls(Object pushClientId) {
        String clientBusCallServiceAddress = SharedClientServerPushInfo.computeClientBusCallServiceAddress(pushClientId);
        Logger.log("Subscribing " + clientBusCallServiceAddress);
        return BusCallService.listenBusEntryCalls(clientBusCallServiceAddress);
    }
}
