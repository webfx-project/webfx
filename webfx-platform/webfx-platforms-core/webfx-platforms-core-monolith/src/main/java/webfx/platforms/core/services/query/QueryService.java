package webfx.platforms.core.services.query;

import webfx.platforms.core.services.bus.call.BusCallService;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.core.services.query.spi.remote.RemoteQueryServiceProviderImpl;
import webfx.platforms.core.util.async.Batch;
import webfx.platforms.core.util.async.Future;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class QueryService {

    public static final String QUERY_SERVICE_ADDRESS = "service/query";
    public static final String QUERY_BATCH_SERVICE_ADDRESS = "service/query/batch";

    static {
        ServiceLoaderHelper.registerDefaultServiceFactory(QueryServiceProvider.class, RemoteQueryServiceProviderImpl::new);
        // registerJsonCodecsAndBusCalls() body:
        QueryArgument.registerJsonCodec();
        QueryResult.registerJsonCodec();
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_SERVICE_ADDRESS, QueryService::executeQuery);
        BusCallService.registerJavaAsyncFunctionAsCallableService(QUERY_BATCH_SERVICE_ADDRESS, QueryService::executeQueryBatch);
    }

    public static void registerJsonCodecsAndBusCalls() {
        // body actually moved to the static constructor
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
