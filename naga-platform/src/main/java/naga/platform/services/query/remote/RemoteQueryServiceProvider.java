package naga.platform.services.query.remote;

import naga.platform.services.query.spi.QueryServiceProvider;
import naga.util.Arrays;
import naga.platform.bus.call.BusCallServerActivity;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.services.query.LocalQueryServiceRegistry;
import naga.platform.services.query.QueryArgument;
import naga.platform.services.query.QueryResultSet;
import naga.util.async.Future;
import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class RemoteQueryServiceProvider implements QueryServiceProvider {

    @Override
    public Future<QueryResultSet> executeQuery(QueryArgument argument) {
        Platform.log("Query: " + argument.getQueryString() + (argument.getParameters() == null ? "" : "\nParameters: " + Arrays.toString(argument.getParameters())));
        QueryServiceProvider localQueryServiceProvider = getConnectedLocalQueryService(argument.getDataSourceId());
        if (localQueryServiceProvider != null)
            return localQueryServiceProvider.executeQuery(argument);
        return executeRemoteQuery(argument);
    }

    protected QueryServiceProvider getConnectedLocalQueryService(Object dataSourceId) {
        QueryServiceProvider connectedQueryServiceProvider = LocalQueryServiceRegistry.getLocalConnectedQueryService(dataSourceId);
        if (connectedQueryServiceProvider == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null) {
                connectedQueryServiceProvider = createConnectedQueryService(connectionDetails);
                LocalQueryServiceRegistry.registerLocalConnectedQueryService(dataSourceId, connectedQueryServiceProvider);
            }
        }
        return connectedQueryServiceProvider;
    }

    protected QueryServiceProvider createConnectedQueryService(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't support local query service");
    }

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument) {
        return BusCallService.call(BusCallServerActivity.QUERY_SERVICE_ADDRESS, argument);
    }
}
