package naga.providers.platform.server.vertx.services.update;

import io.vertx.core.Vertx;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.update.spi.UpdateServiceProvider;
import naga.platform.services.update.spi.remote.RemoteUpdateServiceProviderImpl;
import naga.providers.platform.server.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;

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
