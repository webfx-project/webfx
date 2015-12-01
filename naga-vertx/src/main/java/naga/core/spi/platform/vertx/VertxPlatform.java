package naga.core.spi.platform.vertx;

import io.vertx.core.Vertx;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.vertx.VertxBusFactory;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.vertx.VertxJsonFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.Scheduler;
import naga.core.spi.sched.vertx.VertxScheduler;

import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform implements Platform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
    }

    private final Scheduler scheduler;
    private final JsonFactory jsonFactory = new VertxJsonFactory();
    private final BusFactory busFactory;

    public VertxPlatform(Vertx vertx) {
        scheduler = new VertxScheduler(vertx);
        busFactory = new VertxBusFactory(vertx.eventBus());
    }

    @Override
    public Logger logger() {
        return Logger.getAnonymousLogger();
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
}
