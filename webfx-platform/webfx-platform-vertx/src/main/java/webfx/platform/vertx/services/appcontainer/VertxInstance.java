package webfx.platform.vertx.services.appcontainer;

import io.vertx.core.Vertx;

/**
 * @author Bruno Salmon
 */
public final class VertxInstance {

    private static Vertx VERTX;

    static void setVertx(Vertx vertx) {
        VERTX = vertx;
    }

    public static Vertx getVertx() {
        return VERTX;
    }
}
