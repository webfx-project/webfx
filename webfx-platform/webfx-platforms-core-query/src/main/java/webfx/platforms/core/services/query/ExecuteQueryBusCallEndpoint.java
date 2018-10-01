package webfx.platforms.core.services.query;

import webfx.platforms.core.services.buscall.spi.AsyncFunctionBusCallEndpoint;

/**
 * @author Bruno Salmon
 */
public final class ExecuteQueryBusCallEndpoint extends AsyncFunctionBusCallEndpoint<QueryArgument, QueryResult> {

    public ExecuteQueryBusCallEndpoint() {
        super(QueryService.QUERY_SERVICE_ADDRESS, QueryService::executeQuery);
    }
}
