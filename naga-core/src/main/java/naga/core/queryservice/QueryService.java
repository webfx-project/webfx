package naga.core.queryservice;

import naga.core.util.async.Batch;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface QueryService {

    Future<QueryResultSet> read(QueryArgument argument);

    // Batch support

    default Future<Batch<QueryResultSet>> readBatch(Batch<QueryArgument> batch) {
        return batch.execute(this::read, QueryResultSet.class);
    }

}
