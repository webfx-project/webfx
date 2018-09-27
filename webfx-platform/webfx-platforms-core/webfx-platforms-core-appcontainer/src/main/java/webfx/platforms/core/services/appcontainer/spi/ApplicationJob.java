package webfx.platforms.core.services.appcontainer.spi;

import webfx.platforms.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface ApplicationJob {

    default Future<Void> onStartAsync() { return Future.runAsync(this::onStart); }

    default Future<Void> onStopAsync() { return Future.runAsync(this::onStop); }

    default void onStart() {}

    default void onStop() {}

}
