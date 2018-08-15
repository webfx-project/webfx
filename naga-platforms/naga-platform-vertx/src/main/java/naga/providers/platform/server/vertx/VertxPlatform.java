package naga.providers.platform.server.vertx;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import naga.platform.bus.BusFactory;
import naga.platform.services.json.Json;
import naga.platform.services.query.QueryService;
import naga.platform.services.query.push.QueryPushService;
import naga.platform.services.query.push.spi.impl.InMemoryQueryPushServiceProviderImpl;
import naga.platform.services.scheduler.Scheduler;
import naga.platform.services.update.UpdateService;
import naga.platform.spi.Platform;
import naga.platform.spi.server.ServerModule;
import naga.platform.spi.server.ServerPlatform;
import naga.providers.platform.abstr.java.JavaPlatform;
import naga.providers.platform.server.vertx.bus.VertxBusFactory;
import naga.providers.platform.server.vertx.services.json.VertxJsonObject;
import naga.providers.platform.server.vertx.services.query.VertxQueryServiceProviderImpl;
import naga.providers.platform.server.vertx.services.scheduler.VertxSchedulerProviderImpl;
import naga.providers.platform.server.vertx.services.update.VertxUpdateServiceProviderImpl;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform extends JavaPlatform implements ServerPlatform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
        Scheduler.registerProvider(new VertxSchedulerProviderImpl(vertx));
        Json.registerProvider(new VertxJsonObject());
        QueryService.registerProvider(new VertxQueryServiceProviderImpl(vertx));
        UpdateService.registerProvider(new VertxUpdateServiceProviderImpl(vertx));
        QueryPushService.registerProvider(new InMemoryQueryPushServiceProviderImpl());
    }

    private final BusFactory vertxBusFactory;
    private final Vertx vertx;

    private VertxPlatform(Vertx vertx) {
        vertxBusFactory = new VertxBusFactory(vertx.eventBus());
        this.vertx = vertx;
    }

    @Override
    public BusFactory busFactory() {
        return vertxBusFactory;
    }

    @Override
    public void startServerModule(ServerModule serverModule) {
        vertx.deployVerticle(new Verticle() {
            @Override
            public Vertx getVertx() {
                return vertx;
            }

            @Override
            public void init(Vertx vertx, Context context) {
            }

            @Override
            public void start(Future<Void> future) {
                completeVertxFuture(serverModule.onStart(), future);
            }

            @Override
            public void stop(Future<Void> future) {
                completeVertxFuture(serverModule.onStop(), future);
            }

            private void completeVertxFuture(naga.util.async.Future<Void> nagaFuture, Future<Void> vertxFuture) {
                nagaFuture.setHandler(ar -> {
                    if (ar.failed())
                        vertxFuture.fail(nagaFuture.cause());
                    else
                        vertxFuture.complete();
                });
            }
        });
    }
}
