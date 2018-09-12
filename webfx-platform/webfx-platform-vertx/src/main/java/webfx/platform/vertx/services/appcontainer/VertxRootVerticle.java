package webfx.platform.vertx.services.appcontainer;

import io.vertx.core.AbstractVerticle;

/**
 * @author Bruno Salmon
 */
public class VertxRootVerticle extends AbstractVerticle {

    @Override
    public void start() {
        VertxInstance.setVertx(vertx);
        new VertxApplicationContainerProvider();
        vertx.deployVerticle(new VertxWebVerticle());
    }
}
