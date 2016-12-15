package naga.providers.platform.client.gwt.scheduler;

import com.google.gwt.core.client.JavaScriptObject;
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
    @Override
    protected void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame() {
        if (animationFrameId == null)
            animationFrameId = requestAnimationFrame(this::executeAnimationPipe);
    }

    @Override
    protected void onExecuteAnimationPipeFinished(boolean noMoreAnimationScheduled) {
        animationFrameId = null;
    }

    private static native JavaScriptObject requestAnimationFrame(Runnable runnable) /*-{
        return $wnd.requestAnimationFrame(runnable.@java.lang.Runnable::run().bind(runnable));
    }-*/;
}
