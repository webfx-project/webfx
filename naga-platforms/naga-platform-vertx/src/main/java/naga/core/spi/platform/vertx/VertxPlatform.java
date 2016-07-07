package naga.core.spi.platform.vertx;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import naga.core.activity.ActivityManager;
import naga.core.json.JsonFactory;
import naga.core.services.query.QueryService;
import naga.core.spi.platform.BusFactory;
import naga.core.spi.platform.Platform;
import naga.core.spi.platform.ServerPlatform;
import naga.core.spi.platform.java.JavaPlatform;
import naga.core.services.update.UpdateService;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform extends JavaPlatform implements ServerPlatform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
    }

    private final JsonFactory vertxJsonFactory = new VertxJsonObject();
    private final BusFactory vertxBusFactory;
    private final QueryService vertxQueryService;
    private final UpdateService vertxUpdateService;
    private final Vertx vertx;

    public VertxPlatform(Vertx vertx) {
        super(new VertxScheduler(vertx));
        vertxBusFactory = new VertxBusFactory(vertx.eventBus());
        vertxQueryService = new VertxQueryService(vertx);
        vertxUpdateService = new VertxUpdateService(vertx);
        this.vertx = vertx;
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

    @Override
    public void startServerActivity(ActivityManager serverActivityManager) {
        vertx.deployVerticle(new Verticle() {
            @Override
            public Vertx getVertx() {
                return vertx;
            }

            @Override
            public void init(Vertx vertx, Context context) {
            }

            @Override
            public void start(Future<Void> future) throws Exception {
                serverActivityManager.run();
            }

            @Override
            public void stop(Future<Void> future) throws Exception {
                serverActivityManager.destroy();
            }
        });
    }
}
