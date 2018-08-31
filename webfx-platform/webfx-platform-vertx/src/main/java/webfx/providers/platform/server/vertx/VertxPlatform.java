package webfx.providers.platform.server.vertx;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import webfx.platform.bus.BusFactory;
import webfx.platform.services.json.Json;
import webfx.platform.services.query.QueryService;
import webfx.platform.services.query.push.QueryPushService;
import webfx.platform.services.query.push.spi.impl.InMemoryQueryPushServiceProviderImpl;
import webfx.platform.services.scheduler.Scheduler;
import webfx.platform.services.update.UpdateService;
import webfx.platform.spi.Platform;
import webfx.platform.spi.server.ServerModule;
import webfx.platform.spi.server.ServerPlatform;
import webfx.providers.platform.abstr.java.JavaPlatform;
import webfx.providers.platform.server.vertx.bus.VertxBusFactory;
import webfx.providers.platform.server.vertx.services.json.VertxJsonObject;
import webfx.providers.platform.server.vertx.services.query.VertxQueryServiceProviderImpl;
import webfx.providers.platform.server.vertx.services.scheduler.VertxSchedulerProviderImpl;
import webfx.providers.platform.server.vertx.services.update.VertxUpdateServiceProviderImpl;

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

            private void completeVertxFuture(webfx.util.async.Future<Void> webfxFuture, Future<Void> vertxFuture) {
                webfxFuture.setHandler(ar -> {
                    if (ar.failed())
                        vertxFuture.fail(webfxFuture.cause());
                    else
                        vertxFuture.complete();
                });
            }
        });
    }
}
