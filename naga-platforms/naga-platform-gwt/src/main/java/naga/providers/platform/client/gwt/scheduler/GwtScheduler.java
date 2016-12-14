package naga.providers.platform.client.gwt.scheduler;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;
import naga.commons.scheduler.Scheduled;
import naga.commons.scheduler.Scheduler;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;


/**
 * @author Bruno Salmon
 */
public final class GwtScheduler implements Scheduler {

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleDelay(0, runnable);
    }

    @Override
    public GwtTimerScheduled scheduleDelay(long delayMs, Runnable runnable) {
        Timer timer = createTimer(runnable);
        timer.schedule((int) delayMs);
        return new GwtTimerScheduled(timer);
    }

    @Override
    public GwtTimerScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        Timer timer = createTimer(runnable);
        timer.scheduleRepeating((int) delayMs);
        return new GwtTimerScheduled(timer);
    }

    private static Timer createTimer(Runnable runnable) {
        return new Timer() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    private static class GwtTimerScheduled implements Scheduled {
        private final Timer gwtTimer;

        private GwtTimerScheduled(Timer gwtTimer) {
            this.gwtTimer = gwtTimer;
        }

        @Override
        public boolean cancel() {
            gwtTimer.cancel();
            return true;
        }
    }

    @Override
    public boolean isUiThread() {
        return true;
    }

    @Override
    public Scheduled scheduleAnimationFrame(long delayMs, Runnable runnable) {
        return new AnimationFrameScheduled(delayMs, runnable);
    }

    @Override
    public Scheduled schedulePeriodicAnimationFrame(Runnable runnable) {
        return new AnimationFrameScheduled(runnable);
    }

    private static class AnimationFrameScheduled implements Scheduled {

        /********************************************************************
         *  BEGIN static methods
         ********************************************************************/

        private static JavaScriptObject animationFrameId;
        private static List<AnimationFrameScheduled> animations = new ArrayList<>();

        private static void checkNextAnimationFrameIsScheduled() {
            if (animationFrameId == null && !animations.isEmpty())
                animationFrameId = requestAnimationFrame(AnimationFrameScheduled::executeAnimations);
        }

        private static native JavaScriptObject requestAnimationFrame(Runnable runnable) /*-{
            return $wnd.requestAnimationFrame(runnable.@java.lang.Runnable::run().bind(runnable));
        }-*/;

        private static void executeAnimations() {
            for (Iterator<AnimationFrameScheduled> it = animations.iterator(); it.hasNext(); ) {
                AnimationFrameScheduled scheduled = it.next();
                if (scheduled.isCancelled())
                    it.remove();
                else if (scheduled.shouldExecuteNow()) {
                    scheduled.execute();
                    if (!scheduled.isPeriodic())
                        it.remove();
                }
            }
            animationFrameId = null;
            checkNextAnimationFrameIsScheduled();
        }

        /********************************************************************
         *  END static methods
         ********************************************************************/

        private final Runnable runnable;
        private final Long requestedExecutionTime; // null for a periodic animation, non null otherwise
        private boolean cancelled;

        private AnimationFrameScheduled(Runnable runnable) {
            this(runnable, null);
        }

        private AnimationFrameScheduled(long delayMs, Runnable runnable) {
            this(runnable, System.currentTimeMillis() + delayMs);
        }

        private AnimationFrameScheduled(Runnable runnable, Long requestedExecutionTime) {
            this.runnable = runnable;
            this.requestedExecutionTime = requestedExecutionTime;
            animations.add(this);
            checkNextAnimationFrameIsScheduled();
        }

        @Override
        public boolean cancel() {
            return cancelled = true; // will be removed on next animation frame
        }

        private boolean isCancelled() {
            return cancelled;
        }

        private boolean isPeriodic() {
            return requestedExecutionTime == null;
        }

        private boolean shouldExecuteNow() {
            return isPeriodic() || System.currentTimeMillis() >= requestedExecutionTime;
        }

        private void execute() {
            runnable.run();
        }
    }
}
