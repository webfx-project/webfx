package webfx.platforms.java.services.query;

import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.query.spi.impl.LocalOrRemoteQueryServiceProviderImpl;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.java.services.JdbcConnectedServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class JdbcQueryServiceProviderImpl extends LocalOrRemoteQueryServiceProviderImpl {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProviderImpl(connectionDetails);
    }

}
