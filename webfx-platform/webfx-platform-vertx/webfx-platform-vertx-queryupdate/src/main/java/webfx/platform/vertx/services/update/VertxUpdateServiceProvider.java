package webfx.platform.vertx.services.update;

import webfx.platform.vertx.services.VertxLocalConnectedQueryUpdateServiceProvider;
import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.services.update.spi.impl.LocalUpdateServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxUpdateServiceProvider extends LocalUpdateServiceProvider {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProvider(connectionDetails);
    }

}
