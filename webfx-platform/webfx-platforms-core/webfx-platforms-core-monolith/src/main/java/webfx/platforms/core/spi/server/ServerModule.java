package webfx.platforms.core.spi.server;

import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ServerModule {

    Future<Void> onStart();

    default Future<Void> onStop() {
        return Future.succeededFuture();
    }
}
