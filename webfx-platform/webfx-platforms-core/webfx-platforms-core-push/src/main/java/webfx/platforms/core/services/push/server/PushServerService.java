package webfx.platforms.core.services.push.server;

import webfx.platforms.core.services.bus.Bus;
import webfx.platforms.core.services.push.server.spi.PushServerServiceProvider;
import webfx.platforms.core.services.push.server.spi.impl.PushServerServiceProviderImpl;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class PushServerService {

    static {
        SingleServiceLoader.registerDefaultServiceFactory(PushServerServiceProvider.class, PushServerServiceProviderImpl::new);
    }

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