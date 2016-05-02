package naga.core.activity;

/**
 * @author Bruno Salmon
 */
public interface Activity {

    default void onCreate(ActivityContext context) {}

    default void onStart() {}

    default void onResume() {}

    default void onPause() {}

    default void onStop() {}

    default void onRestart() {}

    default void onDestroy() {}
}
