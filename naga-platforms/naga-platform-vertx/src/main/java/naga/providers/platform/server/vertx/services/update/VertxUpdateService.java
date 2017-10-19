package naga.providers.platform.server.vertx.services.update;

import io.vertx.core.Vertx;
import naga.platform.services.datasource.ConnectionDetails;
import naga.platform.services.update.spi.UpdateService;
import naga.platform.services.update.remote.RemoteUpdateService;
import naga.providers.platform.server.vertx.services.VertxConnectedServiceProvider;

/**
 * @author Bruno Salmon
 */
public class VertxUpdateService extends RemoteUpdateService {

    private final Vertx vertx;

    public VertxUpdateService(Vertx vertx) {
        this.vertx = vertx;
    }

    @Override
    protected UpdateService createConnectedUpdateService(ConnectionDetails connectionDetails) {
        return new VertxConnectedServiceProvider(vertx, connectionDetails);
    }

}
