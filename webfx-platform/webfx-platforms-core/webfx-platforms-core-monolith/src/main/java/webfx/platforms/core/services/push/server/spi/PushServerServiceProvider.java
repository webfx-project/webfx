package webfx.platforms.core.services.push.server.spi;

import webfx.platforms.core.bus.Bus;
import webfx.platforms.core.services.push.server.PushClientDisconnectListener;
import webfx.platforms.core.services.push.ClientPushBusAddressesSharedByBothClientAndServer;
import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface PushServerServiceProvider {

    <T> Future<T> callClientService(String serviceAddress, Object javaArgument, Bus bus, Object pushClientId);

    default Future pingPushClient(Bus bus, Object pushClientId) {
        return callClientService(ClientPushBusAddressesSharedByBothClientAndServer.PUSH_PING_CLIENT_LISTENER_SERVICE_ADDRESS, "Server ping for push client " + pushClientId, bus, pushClientId);
    }

    void addPushClientDisconnectListener(PushClientDisconnectListener listener);

    void removePushClientDisconnectListener(PushClientDisconnectListener listener);

}
