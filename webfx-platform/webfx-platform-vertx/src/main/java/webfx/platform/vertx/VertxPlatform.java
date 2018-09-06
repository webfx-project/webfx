package webfx.platform.vertx;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import webfx.platform.vertx.services.bus.VertxBusServiceProvider;
import webfx.platform.vertx.services.json.VertxJsonObject;
import webfx.platform.vertx.services.query.VertxQueryServiceProviderImpl;
import webfx.platform.vertx.services.scheduler.VertxSchedulerProviderImpl;
import webfx.platform.vertx.services.update.VertxUpdateServiceProviderImpl;
import webfx.platforms.core.services.bus.spi.BusServiceProvider;
import webfx.platforms.core.services.json.Json;
import webfx.platforms.core.services.query.QueryService;
import webfx.platforms.core.services.query.push.QueryPushService;
import webfx.platforms.core.services.query.push.spi.impl.InMemoryQueryPushServiceProviderImpl;
import webfx.platforms.core.services.scheduler.Scheduler;
import webfx.platforms.core.services.update.UpdateService;
import webfx.platforms.core.spi.Platform;
import webfx.platforms.core.spi.server.ServerModule;
import webfx.platforms.core.spi.server.ServerPlatform;
import webfx.platforms.core.util.serviceloader.ServiceLoaderHelper;
import webfx.platforms.java.JavaPlatform;

/**
 * @author Bruno Salmon
 */
public final class VertxPlatform extends JavaPlatform implements ServerPlatform {

    public static void register(Vertx vertx) {
        Platform.register(new VertxPlatform(vertx));
        Scheduler.registerProvider(new VertxSchedulerProviderImpl(vertx));
        Json.registerProvider(new VertxJsonObject());
        ServiceLoaderHelper.cacheServiceInstance(BusServiceProvider.class, new VertxBusServiceProvider(vertx));
        QueryService.registerProvider(new VertxQueryServiceProviderImpl(vertx));
        UpdateService.registerProvider(new VertxUpdateServiceProviderImpl(vertx));
        QueryPushService.registerProvider(new InMemoryQueryPushServiceProviderImpl());
    }

    private final Vertx vertx;

    private VertxPlatform(Vertx vertx) {
        this.vertx = vertx;
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

            private void completeVertxFuture(webfx.platforms.core.util.async.Future<Void> webfxFuture, Future<Void> vertxFuture) {
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
