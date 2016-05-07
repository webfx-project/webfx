package naga.core.activity;

import naga.core.util.async.Future;

/**
 * @author Bruno Salmon
 */
public interface Activity {

    /** Async API **/

    default Future<Void> onCreateAsync(ActivityContext context) { return Future.consumeAsync(this::onCreate, context); }

    default Future<Void> onStartAsync() { return Future.runAsync(this::onStart); }

    default Future<Void> onResumeAsync() { return Future.runAsync(this::onResume); }

    default Future<Void> onPauseAsync() { return Future.runAsync(this::onPause); }

    default Future<Void> onStopAsync() { return Future.runAsync(this::onStop); }

    default Future<Void> onRestartAsync() { return Future.runAsync(this::onRestart); }

    default Future<Void> onDestroyAsync() { return Future.runAsync(this::onDestroy); }


    /** Sync API **/

    default void onCreate(ActivityContext context) {}

    default void onStart() {}

    default void onResume() {}

    default void onPause() {}

    default void onStop() {}

    default void onRestart() {}

    default void onDestroy() {}
}
