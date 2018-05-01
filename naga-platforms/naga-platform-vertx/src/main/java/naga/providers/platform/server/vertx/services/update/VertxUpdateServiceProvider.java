package naga.providers.platform.server.vertx.services.update;

import io.vertx.core.Vertx;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.update.spi.UpdateServiceProvider;
import naga.platform.services.update.spi.remote.RemoteUpdateServiceProvider;
import naga.providers.platform.server.vertx.services.VertxLocalConnectedQueryUpdateServiceProvider;

/**
 * @author Bruno Salmon
 */
public class VertxUpdateServiceProvider extends RemoteUpdateServiceProvider {

    private final Vertx vertx;

    public VertxUpdateServiceProvider(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected UpdateServiceProvider createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProvider(vertx, connectionDetails);
    }

}
