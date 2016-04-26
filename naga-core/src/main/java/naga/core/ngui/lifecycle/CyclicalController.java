package naga.core.ngui.lifecycle;

/**
 * @author Bruno Salmon
 */
public abstract class CyclicalController<C extends Cyclical<CT>, CT extends CyclicalContext> {

    private final C cyclical;

    public CyclicalController(C cyclical, CT context) {
        this.cyclical = cyclical;
        cyclical.onCreate(context);
    }

    public void start() {
        cyclical.onStart();
    }

    public void resume() {
        cyclical.onResume();
    }

    public void pause() {
        cyclical.onPause();
    }

    public void stop() {
        cyclical.onStop();
    }

    public void restart() {
        cyclical.onRestart();
    }

    public void destroy() {
        cyclical.onDestroy();
    }

}
