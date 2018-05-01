package naga.platform.services.querypush;

import naga.platform.services.querypush.spi.remote.RemoteQueryPushServiceProvider;
import naga.platform.services.querypush.spi.QueryPushServiceProvider;
import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class QueryPushService {

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(QueryPushServiceProvider.class, RemoteQueryPushServiceProvider::new);
    }

    public static QueryPushServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(QueryPushServiceProvider.class);
    }

    public static void registerProvider(QueryPushServiceProvider provider) {
        ServiceLoaderHelper.cacheServiceInstance(QueryPushServiceProvider.class, provider);
    }

    public static Future<Object> executeQueryPush(QueryPushArgument argument) {
        return getProvider().executeQueryPush(argument);
    }

    public static void executePulse(PulseArgument argument) {
        getProvider().requestPulse(argument);
    }

}
