package naga.platform.providers.vertx;

import io.vertx.core.Vertx;
import naga.commons.services.datasource.ConnectionDetails;
import naga.commons.services.update.spi.UpdateService;
import naga.commons.services.update.remote.RemoteUpdateService;

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
