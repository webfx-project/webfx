package webfx.providers.platform.abstr.java.services.query;

import webfx.platform.services.datasource.ConnectionDetails;
import webfx.platform.services.query.spi.remote.RemoteQueryServiceProviderImpl;
import webfx.platform.services.query.spi.QueryServiceProvider;
import webfx.providers.platform.abstr.java.services.JdbcConnectedServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class JdbcQueryServiceProviderImpl extends RemoteQueryServiceProviderImpl {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderImpl(connectionDetails);
    }

}
