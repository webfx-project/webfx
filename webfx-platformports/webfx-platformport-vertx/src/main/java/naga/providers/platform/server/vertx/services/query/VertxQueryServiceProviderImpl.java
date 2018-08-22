package naga.providers.platform.server.vertx.services.query;

import io.vertx.core.Vertx;
import naga.platform.services.query.spi.remote.RemoteQueryServiceProviderImpl;
import naga.platform.services.query.spi.QueryServiceProvider;
import naga.platform.services.datasource.ConnectionDetails;
import naga.providers.platform.server.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public class VertxQueryServiceProviderImpl extends RemoteQueryServiceProviderImpl {

    private final Vertx vertx;

    public VertxQueryServiceProviderImpl(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected QueryServiceProvider createLocalConnectedProvider(ConnectionDetails connectionDetails) {
        return new VertxLocalConnectedQueryUpdateServiceProviderImpl(vertx, connectionDetails);
    }

}
