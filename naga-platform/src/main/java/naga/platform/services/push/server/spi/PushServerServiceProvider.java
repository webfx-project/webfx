package naga.platform.services.push.server.spi;

import naga.platform.bus.Bus;
import naga.platform.bus.call.PendingBusCall;

/**
 * @author Bruno Salmon
 */
public interface PushServerServiceProvider {

    <T> PendingBusCall<T> callClientService(String serviceAddress, Object javaArgument, Bus bus, Object pushClientId);

}
