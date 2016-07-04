package naga.core.spi.platform.vertx;

import io.vertx.core.Vertx;
import naga.core.queryservice.impl.ConnectionDetails;
import naga.core.updateservice.UpdateService;
import naga.core.updateservice.impl.RemoteUpdateService;

/**
 * @author Bruno Salmon
 */
class VertxUpdateService extends RemoteUpdateService {

    private final Vertx vertx;

    public VertxUpdateService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected UpdateService createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new VertxConnectedService(vertx, connectionDetails);
    }

}
