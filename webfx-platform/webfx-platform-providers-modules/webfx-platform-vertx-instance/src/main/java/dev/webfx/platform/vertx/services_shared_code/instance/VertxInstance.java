package dev.webfx.platform.vertx.services_shared_code.instance;

import io.vertx.core.Vertx;

/**
 * @author Bruno Salmon
 */
public final class VertxInstance {

    private static Vertx VERTX;

    public static void setVertx(Vertx vertx) {
        VERTX = vertx;
    }

    public static Vertx getVertx() {
        return VERTX;
    }
}
