package webfx.platform.vertx.services.appcontainer;

import webfx.platform.vertx.util.VertxRunner;

/**
 * @author Bruno Salmon
 */
public class VertxApplicationContainerStarter {

    public static void main(String[] args) {
        VertxRunner.runVerticle(VertxRootVerticle.class);
    }
}
