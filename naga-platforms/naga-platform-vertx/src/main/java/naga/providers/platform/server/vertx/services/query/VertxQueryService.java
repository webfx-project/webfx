package naga.providers.platform.server.vertx.services.query;

import io.vertx.core.Vertx;
import naga.platform.services.query.spi.QueryService;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.query.remote.RemoteQueryService;
import naga.providers.platform.server.vertx.services.VertxConnectedService;

/**
 * @author Bruno Salmon
 */
public class VertxQueryService extends RemoteQueryService {

    private final Vertx vertx;

    public VertxQueryService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected QueryService createConnectedQueryService(ConnectionDetails connectionDetails) {
        return new VertxConnectedService(vertx, connectionDetails);
    }

}
