package webfx.platforms.core.services.query.spi.impl;

import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.datasource.LocalDataSourceRegistry;
import webfx.platforms.core.services.log.Logger;
import webfx.platforms.core.services.query.QueryArgument;
import webfx.platforms.core.services.query.QueryResult;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.core.util.Arrays;
import webfx.platforms.core.util.async.Future;

import static webfx.platforms.core.services.query.spi.impl.LocalConnectedQueryServiceProviderRegistry.getLocalConnectedProvider;
import static webfx.platforms.core.services.query.spi.impl.LocalConnectedQueryServiceProviderRegistry.registerLocalConnectedProvider;

/**
 * @author Bruno Salmon
 */
public class LocalQueryServiceProviderImpl implements QueryServiceProvider {

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
        throw new UnsupportedOperationException("This platform doesn't support local query service");
    }

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument) {
        throw new UnsupportedOperationException("This platform doesn't support remote query service");
    }
}
