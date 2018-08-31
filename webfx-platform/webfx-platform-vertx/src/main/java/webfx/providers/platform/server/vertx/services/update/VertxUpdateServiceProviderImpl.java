package webfx.providers.platform.server.vertx.services.update;

import io.vertx.core.Vertx;
import webfx.platform.services.datasource.ConnectionDetails;
import webfx.platform.services.update.spi.UpdateServiceProvider;
import webfx.platform.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import webfx.providers.platform.server.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;

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
