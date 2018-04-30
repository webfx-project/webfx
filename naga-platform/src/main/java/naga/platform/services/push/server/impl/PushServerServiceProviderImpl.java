package naga.platform.services.push.server.impl;

import naga.platform.bus.Bus;
import naga.platform.bus.call.BusCallService;
import naga.platform.bus.call.PendingBusCall;
import naga.platform.services.log.spi.Logger;
import naga.platform.services.push.DefaultClientBusCallAddressComputer;
import naga.platform.services.push.server.spi.PushServerServiceProvider;

/**
 * @author Bruno Salmon
 */
public class PushServerServiceProviderImpl implements PushServerServiceProvider {

    @Override
    public <T> PendingBusCall<T> callClientService(String serviceAddress, Object javaArgument, Bus bus, Object pushClientId) {
        String clientBusCallServiceAddress = DefaultClientBusCallAddressComputer.computeClientBusCallServiceAddress(pushClientId);
        Logger.log("Calling " + clientBusCallServiceAddress);
        return BusCallService.call(clientBusCallServiceAddress, serviceAddress, javaArgument, bus);
    }
}
