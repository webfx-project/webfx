package webfx.platform.shared.services.query.spi.impl.java;

import webfx.platform.shared.datasource.ConnectionDetails;
import webfx.platform.shared.services.query.spi.impl.LocalOrRemoteQueryServiceProvider;
import webfx.platform.shared.services.query.spi.QueryServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class JdbcQueryServiceProvider extends LocalOrRemoteQueryServiceProvider {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new JdbcConnectedServiceProvider(connectionDetails);
    }

}
