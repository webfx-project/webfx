package dev.webfx.platform.shared.services.query;

import dev.webfx.platform.shared.services.buscall.spi.AsyncFunctionBusCallEndpoint;

/**
 * @author Bruno Salmon
 */
public final class ExecuteQueryBusCallEndpoint extends AsyncFunctionBusCallEndpoint<QueryArgument, QueryResult> {

    public ExecuteQueryBusCallEndpoint() {
        super(QueryService.QUERY_SERVICE_ADDRESS, QueryService::executeQuery);
    }
}
