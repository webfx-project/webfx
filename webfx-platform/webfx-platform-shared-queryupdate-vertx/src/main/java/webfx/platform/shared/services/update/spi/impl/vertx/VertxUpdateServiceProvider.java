package webfx.platform.shared.services.update.spi.impl.vertx;

import webfx.platform.shared.services.query.spi.impl.vertx.VertxLocalConnectedQueryUpdateServiceProvider;
import webfx.platform.shared.datasource.ConnectionDetails;
import webfx.platform.shared.services.update.spi.UpdateServiceProvider;
import webfx.platform.shared.services.update.spi.impl.LocalUpdateServiceProvider;

/**
 * @author Bruno Salmon
 */
public final class VertxUpdateServiceProvider extends LocalUpdateServiceProvider {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProvider(connectionDetails);
    }

}
