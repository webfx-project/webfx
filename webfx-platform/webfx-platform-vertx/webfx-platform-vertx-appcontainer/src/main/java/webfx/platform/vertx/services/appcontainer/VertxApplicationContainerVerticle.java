package webfx.platform.vertx.services.appcontainer;

import io.vertx.core.*;
import webfx.platform.vertx.global.VertxInstance;
import webfx.platforms.core.services.appcontainer.ApplicationContainer;
import webfx.platforms.core.services.appcontainer.ApplicationJob;
import webfx.platforms.core.services.appcontainer.spi.ApplicationContainerProvider;
import webfx.platforms.core.services.appcontainer.spi.impl.ApplicationModuleInitializerManager;

/**
 * There are 2 possible entry points:
 *   1) one initiated by the ApplicationContainer (this includes the main() method of this class)
 *   2) one initiated by Vertx when deploying this verticle
 *
 *   In case 1), the verticle is not yet deployed so the container need to deploy it (this will create a second instance of this class)
 *   In case 2), the verticle is deployed but the container is not started
 *
 * @author Bruno Salmon
 */
public final class VertxApplicationContainerVerticle extends AbstractVerticle implements ApplicationContainerProvider {

    private static Object containerInstance;
    private static Object verticleInstance;

    @Override
    public void initialize() { // Entry point 1)
        containerInstance = this;
        if (verticleInstance == null)
            VertxRunner.runVerticle(VertxApplicationContainerVerticle.class);
        ApplicationModuleInitializerManager.initialize();
    }

    @Override
    public void start() { // Entry point 2)
        verticleInstance = this;
        VertxInstance.setVertx(vertx);
        if (containerInstance == null)
            ApplicationContainer.start(new VertxApplicationContainerVerticle(), null);
        vertx.deployVerticle(new VertxWebVerticle());
    }

    @Override
    public void startApplicationJob(ApplicationJob applicationJob) {
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
                completeVertxFuture(applicationJob.onStart(), future);
            }

            @Override
            public void stop(Future<Void> future) {
                completeVertxFuture(applicationJob.onStop(), future);
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

    public static void main(String[] args) {
        ApplicationContainer.start(new VertxApplicationContainerVerticle(), args);
    }
}
