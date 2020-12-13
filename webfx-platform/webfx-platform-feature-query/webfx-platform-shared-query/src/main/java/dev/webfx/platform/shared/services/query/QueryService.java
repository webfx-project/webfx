package dev.webfx.platform.shared.services.query;

import dev.webfx.platform.shared.services.query.spi.QueryServiceProvider;
import dev.webfx.platform.shared.util.async.Batch;
import dev.webfx.platform.shared.util.async.Future;
import dev.webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class QueryService {

    public static final String QUERY_SERVICE_ADDRESS = "service/query";
    public static final String QUERY_BATCH_SERVICE_ADDRESS = "service/query/batch";

    public static QueryServiceProvider getProvider() {
        return SingleServiceProvider.getProvider(QueryServiceProvider.class, () -> ServiceLoader.load(QueryServiceProvider.class));
    }

    public static Future<QueryResult> executeQuery(QueryArgument argument) {
        return getProvider().executeQuery(argument);
    }

    // Batch support

    public static Future<Batch<QueryResult>> executeQueryBatch(Batch<QueryArgument> batch) {
        return getProvider().executeQueryBatch(batch);
    }

}
