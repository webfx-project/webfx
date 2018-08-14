package naga.providers.platform.server.vertx.verticles;

import io.vertx.core.AbstractVerticle;
import naga.platform.bus.call.BusCallServerActivity;
import naga.providers.platform.server.vertx.VertxPlatform;
import naga.providers.platform.server.vertx.util.VertxRunner;

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
        BusCallServerActivity.startAsServerActivity();
    }
}
