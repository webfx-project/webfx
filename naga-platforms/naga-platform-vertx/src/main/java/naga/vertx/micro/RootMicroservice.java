package naga.vertx.micro;

import io.vertx.core.AbstractVerticle;
import naga.core.activity.ActivityManager;
import naga.core.spi.platform.vertx.VertxPlatform;
import naga.vertx.util.VertxRunner;

/**
 * @author Bruno Salmon
 */
public class RootMicroservice extends AbstractVerticle {

    // Convenient method to run the microservice directly in the IDE
    public static void main(String[] args) {
        run();
    }

    public static void run() {
        VertxRunner.runVerticle(RootMicroservice.class);
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new WebMicroservice());
        // We can't use the automatic platform registration mechanism provided by the ServiceLoader here
        // because we need to pass the vertx instance (there is no default constructor for VertxPlatform)
        VertxPlatform.register(vertx); // So we use the explicit registration mechanism instead
        ActivityManager.startBusCallMicroservice();
    }
}
