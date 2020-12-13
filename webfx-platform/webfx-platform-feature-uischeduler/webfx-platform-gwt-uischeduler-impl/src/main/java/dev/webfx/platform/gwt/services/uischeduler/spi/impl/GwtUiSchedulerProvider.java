package dev.webfx.platform.gwt.services.uischeduler.spi.impl;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;
import elemental2.dom.DomGlobal;
import dev.webfx.platform.shared.services.log.Logger;
import dev.webfx.platform.client.services.uischeduler.spi.impl.UiSchedulerProviderBase;


/**
 * @author Bruno Salmon
 */
public final class GwtUiSchedulerProvider extends UiSchedulerProviderBase {

    private static final long MILLIS_IN_NANO = 1_000_000;
    private static final long START_NANO = System.currentTimeMillis() * MILLIS_IN_NANO - performanceNano();

    @Override
    public int availableProcessors() {
        int hardwareConcurrency = DomGlobal.navigator.hardwareConcurrency; // Note: may be undefined if privacy settings refuse to reveal it
        boolean defined = Character.isDigit(("" + hardwareConcurrency /* should return "undefined" or "NaN" if not defined */).charAt(0));
        return defined ? hardwareConcurrency : -1; // returning -1 when undefined
    }

    @Override
    public boolean isUiThread() {
        return true;
    }

    @Override
    public long nanoTime() {
        return START_NANO + performanceNano();
    }

    private static long performanceNano() {
        return (long) (performanceNow() * MILLIS_IN_NANO);
    }

    private static native double performanceNow() /*-{
        return performance.now();
    }-*/;

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

    @Override
    protected void log(String message) {
        Logger.log(message);
    }

    @Override
    protected void log(Throwable throwable) {
        Logger.log(throwable);
    }
}
