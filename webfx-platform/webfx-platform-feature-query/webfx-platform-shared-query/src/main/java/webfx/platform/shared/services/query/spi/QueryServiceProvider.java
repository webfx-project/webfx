package webfx.platform.shared.services.query.spi;

import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.util.async.Batch;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.services.query.QueryArgument;

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
