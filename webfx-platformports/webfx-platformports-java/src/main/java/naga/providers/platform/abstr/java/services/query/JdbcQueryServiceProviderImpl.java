package naga.providers.platform.abstr.java.services.query;

import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.query.spi.remote.RemoteQueryServiceProviderImpl;
import naga.platform.services.query.spi.QueryServiceProvider;
import naga.providers.platform.abstr.java.services.JdbcConnectedServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class JdbcQueryServiceProviderImpl extends RemoteQueryServiceProviderImpl {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderImpl(connectionDetails);
    }

}
