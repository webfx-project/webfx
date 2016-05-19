package naga.core.spi.platform.vertx;

import io.vertx.core.Vertx;
import naga.core.json.JsonFactory;
import naga.core.queryservice.QueryService;
import naga.core.spi.platform.BusFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.java.JavaPlatform;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform extends JavaPlatform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
    }

    private final JsonFactory jsonFactory = new VertxJsonObject();
    private final BusFactory busFactory;
    private final QueryService queryService;

    public VertxPlatform(Vertx vertx) {
        super(new VertxScheduler(vertx));
        busFactory = new VertxBusFactory(vertx.eventBus());
        queryService = new VertxQueryService(vertx);
    }

    @Override
    public JsonFactory jsonFactory() {
        return jsonFactory;
    }

    @Override
    public BusFactory busFactory() {
        return busFactory;
    }

    @Override
    public QueryService queryService() {
        return queryService;
    }
}
