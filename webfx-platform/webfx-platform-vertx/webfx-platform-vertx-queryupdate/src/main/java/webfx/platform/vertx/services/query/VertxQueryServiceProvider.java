package webfx.platform.vertx.services.query;

import webfx.platform.vertx.services.VertxLocalConnectedQueryUpdateServiceProvider;
import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.core.services.query.spi.impl.LocalQueryServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxQueryServiceProvider extends LocalQueryServiceProvider {

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProvider(connectionDetails);
    }

}
