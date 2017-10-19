package naga.providers.platform.server.vertx;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import naga.platform.activity.ActivityManager;
import naga.platform.bus.BusFactory;
import naga.platform.json.Json;
import naga.platform.services.query.spi.QueryService;
import naga.platform.services.update.spi.UpdateService;
import naga.platform.spi.Platform;
import naga.platform.spi.server.ServerPlatform;
import naga.providers.platform.abstr.java.JavaPlatform;
import naga.providers.platform.server.vertx.bus.VertxBusFactory;
import naga.providers.platform.server.vertx.json.VertxJsonObject;
import naga.providers.platform.server.vertx.scheduler.VertxSchedulerProvider;
import naga.providers.platform.server.vertx.services.query.VertxQueryServiceProvider;
import naga.providers.platform.server.vertx.services.update.VertxUpdateService;
import naga.scheduler.Scheduler;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform extends JavaPlatform implements ServerPlatform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
        Scheduler.registerProvider(new VertxSchedulerProvider(vertx));
        Json.registerProvider(new VertxJsonObject());
        QueryService.registerProvider(new VertxQueryServiceProvider(vertx));
    }

    private final BusFactory vertxBusFactory;
    private final UpdateService vertxUpdateService;
    private final Vertx vertx;

    public VertxPlatform(Vertx vertx) {
        vertxBusFactory = new VertxBusFactory(vertx.eventBus());
        vertxUpdateService = new VertxUpdateService(vertx);
        this.vertx = vertx;
    }

    @Override
    public BusFactory busFactory() {
        return vertxBusFactory;
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
