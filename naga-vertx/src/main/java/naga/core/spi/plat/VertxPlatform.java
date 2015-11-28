package naga.core.spi.plat;

import io.vertx.core.Vertx;
import naga.core.spi.bus.BusFactory;
import naga.core.spi.bus.vertx.VertxBusFactory;
import naga.core.spi.json.JsonFactory;
import naga.core.spi.json.vertx.VertxJsonFactory;
import naga.core.spi.sock.WebSocketFactory;

import java.util.logging.Logger;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform implements Platform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
    }

    private final JsonFactory jsonFactory = new VertxJsonFactory();
    private final Scheduler scheduler;
    private final BusFactory busFactory;

    public VertxPlatform(Vertx vertx) {
        scheduler = new VertxScheduler(vertx);
        busFactory = new VertxBusFactory(vertx.eventBus());
    }

    @Override
    public WebSocketFactory webSocketFactory() {
        // never used as the vertx bus implementation is not using it
        throw new UnsupportedOperationException();
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
    public Logger logger() {
        return Logger.getAnonymousLogger();
    }
}
