package naga.platform.services.query.spi;

import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.util.async.Batch;
import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface QueryServiceProvider {

    Future<QueryResultSet> executeQuery(QueryArgument argument);

    // Batch support

    default Future<Batch<QueryResultSet>> executeQueryBatch(Batch<QueryArgument> batch) {
        return batch.executeParallel(QueryResultSet[]::new, this::executeQuery);
    }

}
