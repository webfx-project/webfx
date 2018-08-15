package naga.platform.spi.server;

import naga.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ServerModule {

    Future<Void> onStart();

    default Future<Void> onStop() {
        return Future.succeededFuture();
    }
}
