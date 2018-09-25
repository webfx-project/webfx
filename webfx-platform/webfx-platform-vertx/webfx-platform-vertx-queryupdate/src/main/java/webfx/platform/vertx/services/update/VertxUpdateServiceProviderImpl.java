package webfx.platform.vertx.services.update;

import webfx.platform.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;
import webfx.platforms.core.datasource.ConnectionDetails;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.services.update.spi.impl.LocalUpdateServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class VertxUpdateServiceProviderImpl extends LocalUpdateServiceProviderImpl {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProviderImpl(connectionDetails);
    }

}
