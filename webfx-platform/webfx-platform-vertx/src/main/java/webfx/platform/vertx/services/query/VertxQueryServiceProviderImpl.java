package webfx.platform.vertx.services.query;

import io.vertx.core.Vertx;
import webfx.platforms.core.services.query.spi.remote.RemoteQueryServiceProviderImpl;
import webfx.platforms.core.services.query.spi.QueryServiceProvider;
import webfx.platforms.core.services.datasource.ConnectionDetails;
import webfx.platform.vertx.services.VertxLocalConnectedQueryUpdateServiceProviderImpl;

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
