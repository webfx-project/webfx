package webfx.platform.server.services.push;

import webfx.platform.server.services.push.spi.PushServerServiceProvider;
import webfx.platform.shared.services.bus.Bus;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class PushServerService {

    public static PushServerServiceProvider getProvider() {
        return SingleServiceLoader.loadService(PushServerServiceProvider.class);
    }

    public static <T> Future<T> callClientService(String serviceAddress, Object javaArgument, Bus bus, Object pushClientId) {
        return getProvider().callClientService(serviceAddress, javaArgument, bus, pushClientId);
    }

    public static Future pingPushClient(Bus bus, Object pushClientId) {
        return getProvider().pingPushClient(bus, pushClientId);
    }

    public static void addUnresponsivePushClientListener(UnresponsivePushClientListener listener) {
        getProvider().addUnresponsivePushClientListener(listener);
    }

    public static void removeUnresponsivePushClientListener(UnresponsivePushClientListener listener) {
        getProvider().removeUnresponsivePushClientListener(listener);
    }
}