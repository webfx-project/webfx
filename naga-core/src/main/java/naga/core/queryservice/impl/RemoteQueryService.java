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
        QueryService localQueryService = getConnectedLocalSqlService(argument.getDataSourceId());
        if (localQueryService != null)
            return localQueryService.read(argument);
        return executeRemote(argument, true);
    }

    @Override
    public Future<WriteResult> write(QueryArgument argument) {
        QueryService localQueryService = getConnectedLocalSqlService(argument.getDataSourceId());
        if (localQueryService != null)
            return localQueryService.write(argument);
        return executeRemote(argument, false);
    }

    protected QueryService getConnectedLocalSqlService(Object dataSourceId) {
        QueryService connectedQueryService = LocalDataSourceRegistry.getConnectedSqlService(dataSourceId);
        if (connectedQueryService == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null)
                connectedQueryService = createConnectedSqlService(connectionDetails);
        }
        return connectedQueryService;
    }

    protected QueryService createConnectedSqlService(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't support local sql service");
    }

    protected <T> Future<T> executeRemote(QueryArgument argument, boolean read) {
        return BusCallService.call(read ? Naga.SQL_READ_ADDRESS : Naga.SQL_WRITE_ADDRESS, argument);
    }
}
