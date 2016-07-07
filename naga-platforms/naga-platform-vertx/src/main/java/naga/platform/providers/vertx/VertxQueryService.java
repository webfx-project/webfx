package naga.platform.providers.vertx;

import io.vertx.core.Vertx;
import naga.commons.services.query.spi.QueryService;
import naga.commons.services.datasource.ConnectionDetails;
import naga.commons.services.query.remote.RemoteQueryService;

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
