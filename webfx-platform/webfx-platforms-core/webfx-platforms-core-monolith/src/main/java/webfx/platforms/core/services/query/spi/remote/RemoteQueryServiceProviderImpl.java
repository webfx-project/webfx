package webfx.platforms.core.services.query.spi.remote;

import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.query.QueryResult;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.core.bus.call.BusCallServerModule;
import webfx.platforms.core.bus.call.BusCallService;
import webfx.platforms.core.services.datasource.LocalDataSourceRegistry;
import webfx.platforms.core.services.query.QueryArgument;
import webfx.platforms.core.util.Arrays;
import webfx.platforms.core.services.datasource.ConnectionDetails;
import webfx.platforms.core.util.async.Future;

import static webfx.platforms.core.services.query.spi.remote.LocalConnectedQueryServiceProviderRegistry.*;

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
