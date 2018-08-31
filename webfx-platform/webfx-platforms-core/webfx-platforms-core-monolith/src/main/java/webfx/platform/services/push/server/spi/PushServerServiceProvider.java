package webfx.platform.services.push.server.spi;

import webfx.platform.bus.Bus;
import webfx.platform.services.push.server.PushClientDisconnectListener;
import webfx.util.async.Future;

import static webfx.platform.services.push.ClientPushBusAddressesSharedByBothClientAndServer.PUSH_PING_CLIENT_LISTENER_SERVICE_ADDRESS;

/**
 * @author Bruno Salmon
 */
public interface PushServerServiceProvider {

    <T> Future<T> callClientService(String serviceAddress, Object javaArgument, Bus bus, Object pushClientId);

    default Future pingPushClient(Bus bus, Object pushClientId) {
        return callClientService(PUSH_PING_CLIENT_LISTENER_SERVICE_ADDRESS, "Server ping for push client " + pushClientId, bus, pushClientId);
    }

    void addPushClientDisconnectListener(PushClientDisconnectListener listener);

    void removePushClientDisconnectListener(PushClientDisconnectListener listener);

}
