package webfx.platform.vertx.services.update;

import webfx.platform.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;
import webfx.platforms.core.services.datasource.ConnectionDetails;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.services.update.spi.remote.RemoteUpdateServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class VertxUpdateServiceProviderImpl extends RemoteUpdateServiceProviderImpl {

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProviderImpl(connectionDetails);
    }

}
