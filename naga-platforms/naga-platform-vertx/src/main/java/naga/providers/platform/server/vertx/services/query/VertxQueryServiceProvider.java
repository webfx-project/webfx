package naga.providers.platform.server.vertx.services.query;

import io.vertx.core.Vertx;
import naga.platform.services.query.remote.RemoteQueryServiceProvider;
import naga.platform.services.query.spi.QueryServiceProvider;
import naga.platform.services.datasource.ConnectionDetails;
import naga.providers.platform.server.vertx.services.VertxLocalConnectedQueryUpdateServiceProvider;

/**
 * @author Bruno Salmon
 */
public class VertxQueryServiceProvider extends RemoteQueryServiceProvider {

    private final Vertx vertx;

    public VertxQueryServiceProvider(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProvider(vertx, connectionDetails);
    }

}
