package webfx.platforms.java.services.query;

import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.query.spi.impl.LocalOrRemoteQueryServiceProvider;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.java.services.JdbcConnectedServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class JdbcQueryServiceProvider extends LocalOrRemoteQueryServiceProvider {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProvider(connectionDetails);
    }

}
