package naga.core.spi.platform.vertx;

import io.vertx.core.Vertx;
import naga.core.json.JsonFactory;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.vertx.VertxBusFactory;
import naga.core.spi.json.vertx.VertxJsonObject;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.sql.SqlService;
import naga.core.spi.sql.vertx.VertxSqlService;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform extends Platform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
    }

    private final Scheduler scheduler;
    private final JsonFactory jsonFactory = new VertxJsonObject();
    private final BusFactory busFactory;
    private final SqlService sqlService;

    public VertxPlatform(Vertx vertx) {
        scheduler = new VertxScheduler(vertx);
        busFactory = new VertxBusFactory(vertx.eventBus());
        sqlService = new VertxSqlService(vertx);
    }

    @Override
    public Scheduler scheduler() {
        return scheduler;
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
    public SqlService sqlService() {
        return sqlService;
    }
}
