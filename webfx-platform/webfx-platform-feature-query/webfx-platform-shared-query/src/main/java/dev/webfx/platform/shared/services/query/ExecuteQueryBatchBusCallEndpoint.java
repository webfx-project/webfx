package dev.webfx.platform.shared.services.query;

import dev.webfx.platform.shared.services.buscall.spi.AsyncFunctionBusCallEndpoint;
import dev.webfx.platform.shared.util.async.Batch;

/**
 * @author Bruno Salmon
 */
public final class ExecuteQueryBatchBusCallEndpoint extends AsyncFunctionBusCallEndpoint<Batch<QueryArgument>, Batch<QueryResult>> {

    public ExecuteQueryBatchBusCallEndpoint() {
        super(QueryService.QUERY_BATCH_SERVICE_ADDRESS, QueryService::executeQueryBatch);
    }
}
