package naga.platform.services.query.spi;

import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.util.async.Batch;
import naga.util.async.Future;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class QueryService {

    private static QueryServiceProvider PROVIDER;

    public static QueryServiceProvider getProvider() {
        if (PROVIDER == null)
            registerProvider(ServiceLoaderHelper.loadService(QueryServiceProvider.class));
        return PROVIDER;
    }

    public static void registerProvider(QueryServiceProvider provider) {
        PROVIDER = provider;
    }

    public static Future<QueryResultSet> executeQuery(QueryArgument argument) {
        return getProvider().executeQuery(argument);
    }

    // Batch support

    public static Future<Batch<QueryResultSet>> executeQueryBatch(Batch<QueryArgument> batch) {
        return getProvider().executeQueryBatch(batch);
    }

}
