package webfx.platform.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import webfx.platforms.core.services.bus.call.BusCallServerModule;
import webfx.platform.vertx.VertxPlatform;
import webfx.platform.vertx.util.VertxRunner;

/**
 * @author Bruno Salmon
 */
public class RootVerticle extends AbstractVerticle {

    // Convenient method to run the microservice directly in the IDE
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        VertxRunner.runVerticle(RootVerticle.class);
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new WebVerticle());
        // We can't use the automatic platform registration mechanism provided by the ServiceLoader here
        // because we need to pass the vertx instance (there is no default constructor for VertxPlatform)
        VertxPlatform.register(vertx); // So we use the explicit registration mechanism instead
        // Starting the server module that listen the bus calls so clients can communicate with the server
        BusCallServerModule.start();
    }
}
