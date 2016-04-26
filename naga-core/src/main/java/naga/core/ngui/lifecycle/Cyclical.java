package naga.core.ngui.lifecycle;

/**
 * @author Bruno Salmon
 */
public interface Cyclical<C extends CyclicalContext> {

    default void onCreate(C context) {}

    default void onStart() {}

    default void onResume() {}

    default void onPause() {}

    default void onStop() {}

    default void onRestart() {}

    default void onDestroy() {}
}
