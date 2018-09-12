package webfx.platform.vertx.services.appcontainer;

import io.vertx.core.Context;
import io.vertx.core.Future;
import io.vertx.core.Verticle;
import io.vertx.core.Vertx;
import webfx.platforms.core.services.appcontainer.ApplicationService;
import webfx.platforms.core.services.appcontainer.spi.impl.ApplicationContainerProviderBase;

/**
 * @author Bruno Salmon
 */
public class VertxApplicationContainerProvider extends ApplicationContainerProviderBase {

    @Override
    public void startApplicationService(ApplicationService applicationService) {
        Vertx vertx = VertxInstance.getVertx();
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
                completeVertxFuture(applicationService.onStart(), future);
            }

            @Override
            public void stop(Future<Void> future) {
                completeVertxFuture(applicationService.onStop(), future);
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
