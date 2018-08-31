package webfx.providers.platform.server.vertx.services.query;

import io.vertx.core.Vertx;
import webfx.platform.services.query.spi.remote.RemoteQueryServiceProviderImpl;
import webfx.platform.services.query.spi.QueryServiceProvider;
import webfx.platform.services.datasource.ConnectionDetails;
import webfx.providers.platform.server.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;

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
