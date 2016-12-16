package naga.providers.platform.client.gwt.scheduler;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;
import naga.commons.scheduler.impl.UiSchedulerBase;


/**
 * @author Bruno Salmon
 */
public final class GwtScheduler extends UiSchedulerBase {

    @Override
    public boolean isUiThread() {
        return true;
    }

    private static JavaScriptObject animationFrameId;
    private static Timer checkTimer;
    @Override
    protected void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame() {
        if (animationFrameId == null) {
            animationFrameId = requestAnimationFrame(this::executeAnimationPipe);
            // Additional checker in case the animation frame is not honored anymore (this can happen when the browser
            // window is viewed again after having being minimized or when switching back from another browser tab)
            checkTimer = new Timer() {
                @Override
                public void run() {
                    executeAnimationPipe(); // This will execute not honored animations
                }
            };
            checkTimer.scheduleRepeating(1000); // checking every 1s should be enough
        }
    }

    @Override
    protected void onExecuteAnimationPipeFinished(boolean noMoreAnimationScheduled) {
        animationFrameId = null;
        if (checkTimer != null) {
            checkTimer.cancel();
            checkTimer = null;
        }
    }

    private static native JavaScriptObject requestAnimationFrame(Runnable runnable) /*-{
        return $wnd.requestAnimationFrame(runnable.@java.lang.Runnable::run().bind(runnable));
    }-*/;
}
