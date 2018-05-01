package naga.platform.services.push.server;

import naga.platform.bus.Bus;
import naga.platform.bus.call.PendingBusCall;
import naga.platform.services.push.server.spi.impl.PushServerServiceProviderImpl;
import naga.platform.services.push.server.spi.PushServerServiceProvider;
import naga.util.serviceloader.ServiceLoaderHelper;

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

    public static <T> PendingBusCall<T> callClientService(String serviceAddress, Object javaArgument, Bus bus, Object pushClientId) {
        return getProvider().callClientService(serviceAddress, javaArgument, bus, pushClientId);
    }

}
