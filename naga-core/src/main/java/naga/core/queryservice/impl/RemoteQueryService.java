package naga.core.queryservice.impl;

import naga.core.Naga;
import naga.core.bus.call.BusCallService;
import naga.core.queryservice.QueryArgument;
import naga.core.queryservice.QueryResultSet;
import naga.core.queryservice.QueryService;
import naga.core.queryservice.WriteResult;
import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class RemoteQueryService implements QueryService {

    public static RemoteQueryService REMOTE_ONLY_QUERY_SERVICE = new RemoteQueryService();

    @Override
    public Future<QueryResultSet> read(QueryArgument argument) {
        QueryService localQueryService = getConnectedLocalQueryService(argument.getDataSourceId());
        if (localQueryService != null)
            return localQueryService.read(argument);
        return executeRemoteQuery(argument, true);
    }

    @Override
    public Future<WriteResult> write(QueryArgument argument) {
        QueryService localQueryService = getConnectedLocalQueryService(argument.getDataSourceId());
        if (localQueryService != null)
            return localQueryService.write(argument);
        return executeRemoteQuery(argument, false);
    }

    protected QueryService getConnectedLocalQueryService(Object dataSourceId) {
        QueryService connectedQueryService = LocalDataSourceRegistry.getConnectedSqlService(dataSourceId);
        if (connectedQueryService == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null)
                connectedQueryService = createConnectedQueryService(connectionDetails);
        }
        return connectedQueryService;
    }

    protected QueryService createConnectedQueryService(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't support local query service");
    }

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument, boolean read) {
        return BusCallService.call(read ? Naga.QUERY_READ_ADDRESS : Naga.QUERY_WRITE_ADDRESS, argument);
    }
}
