package naga.platform.services.query.spi;

import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.services.query.remote.RemoteQueryServiceProvider;
import naga.util.async.Batch;
import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class QueryService {

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(QueryServiceProvider.class, RemoteQueryServiceProvider::new);
    }

    public static QueryServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(QueryServiceProvider.class);
    }

    public static void registerProvider(QueryServiceProvider provider) {
        ServiceLoaderHelper.cacheServiceInstance(QueryServiceProvider.class, provider);
    }

    public static Future<QueryResultSet> executeQuery(QueryArgument argument) {
        return getProvider().executeQuery(argument);
    }

    // Batch support

    public static Future<Batch<QueryResultSet>> executeQueryBatch(Batch<QueryArgument> batch) {
        return getProvider().executeQueryBatch(batch);
    }

}
