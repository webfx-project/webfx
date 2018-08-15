package naga.platform.services.query.spi.remote;

import naga.platform.services.log.Logger;
import naga.platform.services.query.QueryResult;
import naga.platform.services.query.spi.QueryServiceProvider;
import naga.util.Arrays;
import naga.platform.bus.call.BusCallServerModule;
import naga.platform.bus.call.BusCallService;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.datasource.LocalDataSourceRegistry;
import naga.platform.services.query.QueryArgument;
import naga.util.async.Future;

import static naga.platform.services.query.spi.remote.LocalConnectedQueryServiceProviderRegistry.*;

/**
 * @author Bruno Salmon
 */
public class RemoteQueryServiceProviderImpl implements QueryServiceProvider {

    @Override
    public Future<QueryResult> executeQuery(QueryArgument argument) {
        String message = "Query: " + argument.getQueryString() + (argument.getParameters() == null ? "" : "\nParameters: " + Arrays.toString(argument.getParameters()));
        Logger.log(message);
        QueryServiceProvider localConnectedProvider = getOrCreateLocalConnectedProvider(argument.getDataSourceId());
        if (localConnectedProvider != null)
            return localConnectedProvider.executeQuery(argument);
        return executeRemoteQuery(argument);
    }

    protected QueryServiceProvider getOrCreateLocalConnectedProvider(Object dataSourceId) {
        QueryServiceProvider localConnectedProvider = getLocalConnectedProvider(dataSourceId);
        if (localConnectedProvider == null) {
            ConnectionDetails connectionDetails = LocalDataSourceRegistry.getLocalDataSourceConnectionDetails(dataSourceId);
            if (connectionDetails != null) {
                localConnectedProvider = createLocalConnectedProvider(connectionDetails);
                registerLocalConnectedProvider(dataSourceId, localConnectedProvider);
            }
        }
        return localConnectedProvider;
    }

    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        throw new UnsupportedOperationException("This platform doesn't provide local QueryServiceProvider");
    }

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument) {
        return BusCallService.call(BusCallServerModule.QUERY_SERVICE_ADDRESS, argument);
    }
}
