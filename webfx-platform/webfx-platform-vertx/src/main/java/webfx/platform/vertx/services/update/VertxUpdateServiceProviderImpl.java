package webfx.platform.vertx.services.update;

import io.vertx.core.Vertx;
import webfx.platforms.core.services.datasource.ConnectionDetails;
import webfx.platforms.core.services.update.spi.UpdateServiceProvider;
import webfx.platforms.core.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import webfx.platform.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class VertxUpdateServiceProviderImpl extends RemoteUpdateServiceProviderImpl {

    private final Vertx vertx;

    public VertxUpdateServiceProviderImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProviderImpl(vertx, connectionDetails);
    }

}
