package naga.core.services.query;

import naga.core.util.async.Batch;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface QueryService {

    Future<QueryResultSet> executeQuery(QueryArgument argument);

    // Batch support

    default Future<Batch<QueryResultSet>> executeQueryBatch(Batch<QueryArgument> batch) {
        return batch.execute(this::executeQuery, QueryResultSet.class);
    }

}
