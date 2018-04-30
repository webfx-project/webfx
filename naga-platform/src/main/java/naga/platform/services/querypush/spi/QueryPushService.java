package naga.platform.services.querypush.spi;

import naga.platform.services.querypush.PulseArgument;
import naga.platform.services.querypush.QueryPushArgument;
import naga.platform.services.querypush.remote.RemoteQueryPushServiceProvider;
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

    public static Future<Integer> executePulse(PulseArgument argument) {
        return getProvider().executePulse(argument);
    }

}
