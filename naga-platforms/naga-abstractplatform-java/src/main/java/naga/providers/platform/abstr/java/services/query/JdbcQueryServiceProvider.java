package naga.providers.platform.abstr.java.services.query;

import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.query.remote.RemoteQueryServiceProvider;
import naga.platform.services.query.spi.QueryServiceProvider;
import naga.providers.platform.abstr.java.services.JdbcConnectedServiceProviderProvider;

/**
 * @author Bruno Salmon
 */
public class JdbcQueryServiceProvider extends RemoteQueryServiceProvider {

    @Override
    protected QueryServiceProvider createConnectedQueryService(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderProvider(connectionDetails);
    }

}
