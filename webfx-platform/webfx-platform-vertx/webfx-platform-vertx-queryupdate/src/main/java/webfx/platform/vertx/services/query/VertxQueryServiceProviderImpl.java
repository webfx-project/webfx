package webfx.platform.vertx.services.query;

import webfx.platform.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;
import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.core.services.query.spi.impl.LocalQueryServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class VertxQueryServiceProviderImpl extends LocalQueryServiceProviderImpl {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProviderImpl(connectionDetails);
    }

}
