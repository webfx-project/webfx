package webfx.platform.spi.server;

import webfx.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ServerModule {

    Future<Void> onStart();

    default Future<Void> onStop() {
        return Future.succeededFuture();
    }
}
