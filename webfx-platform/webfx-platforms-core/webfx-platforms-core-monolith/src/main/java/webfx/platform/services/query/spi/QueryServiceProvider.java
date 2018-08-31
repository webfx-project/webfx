package webfx.platform.services.query.spi;

import webfx.platform.services.query.QueryArgument;
import webfx.platform.services.query.QueryResult;
import webfx.util.async.Batch;
import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface QueryServiceProvider {

    Future<QueryResult> executeQuery(QueryArgument argument);

    // Batch support

    default Future<Batch<QueryResult>> executeQueryBatch(Batch<QueryArgument> batch) {
        return batch.executeParallel(QueryResult[]::new, this::executeQuery);
    }

}
