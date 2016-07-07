package naga.commons.services.query.spi;

import naga.commons.services.query.QueryArgument;
import naga.commons.services.query.QueryResultSet;
import naga.commons.util.async.Batch;
import naga.commons.util.async.Future;

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
