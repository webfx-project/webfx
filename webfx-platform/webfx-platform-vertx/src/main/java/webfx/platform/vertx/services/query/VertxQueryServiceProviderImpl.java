package webfx.platform.vertx.services.query;

import webfx.platforms.core.services.query.spi.remote.RemoteQueryServiceProviderImpl;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.core.services.datasource.ConnectionDetails;
import webfx.platform.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class VertxQueryServiceProviderImpl extends RemoteQueryServiceProviderImpl {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProviderImpl(connectionDetails);
    }

}
