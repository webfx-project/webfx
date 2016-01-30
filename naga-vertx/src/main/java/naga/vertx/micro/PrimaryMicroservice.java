package naga.vertx.micro;

import io.vertx.core.AbstractVerticle;
import naga.vertx.util.VertxRunner;

/**
 * @author Bruno Salmon
 */
public class PrimaryMicroservice extends AbstractVerticle {

    // Convenient method to run the microservice directly in the IDE
    public static void main(String[] args) {
        VertxRunner.runVerticle(PrimaryMicroservice.class);
    }

    @Override
    public void start() throws Exception {
        vertx.deployVerticle(new WebMicroservice());
        vertx.deployVerticle(new NagaMicroservice());
    }

}
