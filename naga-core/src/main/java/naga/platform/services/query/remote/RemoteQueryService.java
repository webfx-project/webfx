package naga.platform.services.query.remote;

import naga.platform.bus.call.BusCallServerActivity;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.services.query.LocalQueryServiceRegistry;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.platform.services.query.spi.QueryService;
import naga.commons.util.async.Future;

/**
 * @author Bruno Salmon
 */
public class RemoteQueryService implements QueryService {

    public final static RemoteQueryService REMOTE_ONLY_QUERY_SERVICE = new RemoteQueryService();

    @Override
    public Future<QueryResultSet> executeQuery(QueryArgument argument) {
        QueryService localQueryService = getConnectedLocalQueryService(argument.getDataSourceId());
        if (localQueryService != null)
            return localQueryService.executeQuery(argument);
        return executeRemoteQuery(argument);
    }

    protected QueryService getConnectedLocalQueryService(Object dataSourceId) {
        QueryService connectedQueryService = LocalQueryServiceRegistry.getLocalConnectedQueryService(dataSourceId);
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

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument) {
        return BusCallService.call(BusCallServerActivity.QUERY_SERVICE_ADDRESS, argument);
    }
}
