package dev.webfx.platform.vertx.services.appcontainer.spi.impl;

import io.vertx.core.*;
import dev.webfx.platform.shared.services.appcontainer.ApplicationContainer;
import dev.webfx.platform.vertx.services_shared_code.instance.VertxInstance;
import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationContainerProvider;
import dev.webfx.platform.shared.services.appcontainer.spi.ApplicationJob;
import dev.webfx.platform.shared.services.appcontainer.spi.impl.ApplicationModuleInitializerManager;
import dev.webfx.platform.shared.services.shutdown.Shutdown;

import java.util.ArrayList;
import java.util.Collection;

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

    private static VertxApplicationContainerVerticle containerInstance;
    private static VertxApplicationContainerVerticle verticleInstance;

    private final Collection<ApplicationJobVerticle> applicationJobVerticles = new ArrayList<>();

    @Override
    public void initialize() { // Entry point 1)
        containerInstance = this;
        if (verticleInstance == null)
            VertxRunner.runVerticle(VertxApplicationContainerVerticle.class);
        ApplicationModuleInitializerManager.initialize();
        Shutdown.addShutdownHook(() -> {
            for (String deploymentId : VertxInstance.getVertx().deploymentIDs())
                VertxInstance.getVertx().undeploy(deploymentId);
            ApplicationModuleInitializerManager.shutdown();
            VertxInstance.getVertx().close();
        });
    }

    @Override
    public void start() { // Entry point 2)
        verticleInstance = this;
        VertxInstance.setVertx(vertx);
        if (containerInstance == null)
            ApplicationContainer.main(null);
        vertx.deployVerticle(new VertxWebVerticle());
    }

    @Override
    public void stop() {
        if (this == containerInstance && !Shutdown.isShuttingDown())
            Shutdown.softwareShutdown(false, 0);
    }

    @Override
    public void startApplicationJob(ApplicationJob applicationJob) {
        ApplicationJobVerticle applicationJobVerticle = new ApplicationJobVerticle(applicationJob);
        applicationJobVerticles.add(applicationJobVerticle);
        VertxInstance.getVertx().deployVerticle(applicationJobVerticle, ar -> applicationJobVerticle.deploymentId = ar.result());
    }

    @Override
    public void stopApplicationJob(ApplicationJob applicationJob) {
        applicationJobVerticles.stream()
                .filter(v -> v.applicationJob == applicationJob)
                .findFirst()
                .ifPresent(v -> VertxInstance.getVertx().undeploy(v.deploymentId));
    }

    private final class ApplicationJobVerticle implements Verticle {

        private final ApplicationJob applicationJob;
        private String deploymentId;

        public ApplicationJobVerticle(ApplicationJob applicationJob) {
            this.applicationJob = applicationJob;
        }

        @Override
        public Vertx getVertx() {
            return VertxInstance.getVertx();
        }

        @Override
        public void init(Vertx vertx, Context context) {
        }

        @Override
        public void start(Future<Void> future) {
            completeVertxFuture(applicationJob.onStartAsync(), future);
        }

        @Override
        public void stop(Future<Void> future) {
            completeVertxFuture(applicationJob.onStopAsync(), future);
        }

        private void completeVertxFuture(dev.webfx.platform.shared.util.async.Future<Void> webfxFuture, Future<Void> vertxFuture) {
            webfxFuture.setHandler(ar -> {
                if (ar.failed())
                    vertxFuture.fail(webfxFuture.cause());
                else
                    vertxFuture.complete();
            });
        }
    }

    public static void main(String[] args) {
        ApplicationContainer.main(args);
    }
}
