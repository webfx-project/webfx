package webfx.platforms.core.services.appcontainer.spi;

import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ApplicationJob {

    Future<Void> onStart();

    default Future<Void> onStop() {
        return Future.succeededFuture();
    }
}
