package naga.core.spi.platform.vertx;

import io.vertx.core.Vertx;
import naga.core.json.JsonFactory;
import naga.core.queryservice.QueryService;
import naga.core.spi.platform.BusFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.java.JavaPlatform;
import naga.core.updateservice.UpdateService;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform extends JavaPlatform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
    }

    private final JsonFactory vertxJsonFactory = new VertxJsonObject();
    private final BusFactory vertxBusFactory;
    private final QueryService vertxQueryService;
    private final UpdateService vertxUpdateService;

    public VertxPlatform(Vertx vertx) {
        super(new VertxScheduler(vertx));
        vertxBusFactory = new VertxBusFactory(vertx.eventBus());
        vertxQueryService = new VertxQueryService(vertx);
        vertxUpdateService = new VertxUpdateService(vertx);
    }

    @Override
    public JsonFactory jsonFactory() {
        return vertxJsonFactory;
    }

    @Override
    public BusFactory busFactory() {
        return vertxBusFactory;
    }

    @Override
    public QueryService queryService() {
        return vertxQueryService;
    }

    @Override
    public UpdateService updateService() {
        return vertxUpdateService;
    }
}
