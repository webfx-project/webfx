package webfx.platform.services.push.server;

import webfx.platform.bus.Bus;
import webfx.platform.services.push.server.spi.PushServerServiceProvider;
import webfx.platform.services.push.server.spi.impl.PushServerServiceProviderImpl;
import webfx.util.async.Future;
import webfx.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class PushServerService {

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(PushServerServiceProvider.class, PushServerServiceProviderImpl::new);
    }

    public static PushServerServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(PushServerServiceProvider.class);
    }

    public static <T> Future<T> callClientService(String serviceAddress, Object javaArgument, Bus bus, Object pushClientId) {
        return getProvider().callClientService(serviceAddress, javaArgument, bus, pushClientId);
    }

    public static Future pingPushClient(Bus bus, Object pushClientId) {
        return getProvider().pingPushClient(bus, pushClientId);
    }

    public static void addPushClientDisconnectListener(PushClientDisconnectListener listener) {
        getProvider().addPushClientDisconnectListener(listener);
    }

    public static void removePushClientDisconnectListener(PushClientDisconnectListener listener) {
        getProvider().removePushClientDisconnectListener(listener);
    }
}