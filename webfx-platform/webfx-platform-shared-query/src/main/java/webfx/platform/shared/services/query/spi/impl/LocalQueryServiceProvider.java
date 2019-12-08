package webfx.platform.shared.services.query.spi.impl;

import webfx.platform.shared.service.datasource.LocalDataSource;
import webfx.platform.shared.services.query.QueryArgument;
import webfx.platform.shared.services.query.QueryResult;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;
import webfx.platform.shared.util.Arrays;
import webfx.platform.shared.util.async.Future;
import webfx.platform.shared.services.log.Logger;

import static webfx.platform.shared.services.query.spi.impl.LocalConnectedQueryServiceProviderRegistry.getLocalConnectedProvider;
import static webfx.platform.shared.services.query.spi.impl.LocalConnectedQueryServiceProviderRegistry.registerLocalConnectedProvider;

/**
 * @author Bruno Salmon
 */
public class LocalQueryServiceProvider implements QueryServiceProvider {

    @Override
    public Future<QueryResult> executeQuery(QueryArgument argument) {
        Object dataSourceId = argument.getDataSourceId();
        String queryString = argument.getQueryString();
        Logger.log("Query: " + queryString + (argument.getParameters() == null ? "" : "\nParameters: " + Arrays.toString(argument.getParameters())));
        QueryServiceProvider localConnectedProvider = getOrCreateLocalConnectedProvider(dataSourceId);
        if (localConnectedProvider != null) {
            String queryLang = argument.getQueryLang();
            if (queryLang != null) {
                String translatedQuery = LocalDataSource.get(dataSourceId).translateQueryIntoDataSourceDefaultLanguage(queryLang, queryString);
                if (!queryString.equals(translatedQuery)) {
                    Logger.log("Translated to: " + translatedQuery);
                    argument = new QueryArgument(dataSourceId, translatedQuery, argument.getParameters());
                }
            }
            return localConnectedProvider.executeQuery(argument);
        }
        return executeRemoteQuery(argument);
    }

    protected QueryServiceProvider getOrCreateLocalConnectedProvider(Object dataSourceId) {
        QueryServiceProvider localConnectedProvider = getLocalConnectedProvider(dataSourceId);
        if (localConnectedProvider == null) {
            LocalDataSource localDataSource = LocalDataSource.get(dataSourceId);
            if (localDataSource != null) {
                localConnectedProvider = createLocalConnectedProvider(localDataSource);
                registerLocalConnectedProvider(dataSourceId, localConnectedProvider);
            }
        }
        return localConnectedProvider;
    }

    protected QueryServiceProvider createLocalConnectedProvider(LocalDataSource localDataSource) {
        throw new UnsupportedOperationException("This platform doesn't support local query service");
    }

    protected <T> Future<T> executeRemoteQuery(QueryArgument argument) {
        throw new UnsupportedOperationException("This platform doesn't support remote query service");
    }
}
