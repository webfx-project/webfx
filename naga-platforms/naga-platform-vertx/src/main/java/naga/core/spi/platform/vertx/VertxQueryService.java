package naga.core.spi.platform.vertx;

import io.vertx.core.Vertx;
import naga.core.queryservice.QueryService;
import naga.core.datasource.ConnectionDetails;
import naga.core.queryservice.impl.RemoteQueryService;

/**
 * @author Bruno Salmon
 */
class VertxQueryService extends RemoteQueryService {

    private final Vertx vertx;

    public VertxQueryService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected QueryService createConnectedQueryService(ConnectionDetails connectionDetails) {
        return new VertxConnectedService(vertx, connectionDetails);
    }

}
