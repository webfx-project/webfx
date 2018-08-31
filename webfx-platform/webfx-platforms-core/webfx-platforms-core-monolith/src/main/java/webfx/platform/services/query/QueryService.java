package webfx.platform.services.query;

import webfx.platform.services.query.spi.remote.RemoteQueryServiceProviderImpl;
import webfx.platform.services.query.spi.QueryServiceProvider;
import webfx.util.async.Batch;
import webfx.util.async.Future;
import webfx.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class QueryService {

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(QueryServiceProvider.class, RemoteQueryServiceProviderImpl::new);
    }

    public static QueryServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(QueryServiceProvider.class);
    }

    public static void registerProvider(QueryServiceProvider provider) {
        ServiceLoaderHelper.cacheServiceInstance(QueryServiceProvider.class, provider);
    }

    public static Future<QueryResult> executeQuery(QueryArgument argument) {
        return getProvider().executeQuery(argument);
    }

    // Batch support

    public static Future<Batch<QueryResult>> executeQueryBatch(Batch<QueryArgument> batch) {
        return getProvider().executeQueryBatch(batch);
    }

}
