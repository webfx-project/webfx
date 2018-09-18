package webfx.platforms.core.services.query;

import webfx.platforms.core.services.buscall.spi.AsyncFunctionBusCallEndpoint;
import webfx.platforms.core.util.async.Batch;

/**
 * @author Bruno Salmon
 */
public class ExecuteQueryBatchBusCallEndpoint extends AsyncFunctionBusCallEndpoint<Batch<QueryArgument>, Batch<QueryResult>> {

    public ExecuteQueryBatchBusCallEndpoint() {
        super(QueryService.QUERY_BATCH_SERVICE_ADDRESS, QueryService::executeQueryBatch);
    }
}
