package naga.platform.services.query.spi;

import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResult;
import naga.util.async.Batch;
import naga.util.async.Future;

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
