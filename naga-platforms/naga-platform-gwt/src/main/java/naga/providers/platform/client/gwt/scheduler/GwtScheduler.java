package naga.providers.platform.client.gwt.scheduler;

import com.google.gwt.core.client.JavaScriptObject;
import com.google.gwt.user.client.Timer;
import naga.commons.scheduler.Scheduled;
import naga.commons.scheduler.Scheduler;


/**
 * @author Bruno Salmon
 */
public final class GwtScheduler implements Scheduler {

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleDelay(0, runnable);
    }

    @Override
    public GwtScheduled scheduleDelay(long delayMs, Runnable runnable) {
        Timer timer = createTimer(runnable);
        timer.schedule((int) delayMs);
        return new GwtScheduled(timer);
    }

    @Override
    public GwtScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        Timer timer = createTimer(runnable);
        timer.scheduleRepeating((int) delayMs);
        return new GwtScheduled(timer);
    }

    private static Timer createTimer(Runnable runnable) {
        return new Timer() {
            @Override
            public void run() {
                runnable.run();
            }
        };
    }

    @Override
    public boolean isUiThread() {
        return true;
    }

    @Override
    public Scheduled scheduleAnimationFrame(long delayMs, Runnable runnable) {
        if (delayMs > 0)
            return scheduleDelay(delayMs, runnable);
        return schedulePulse(runnable, false);
    }

    @Override
    public Scheduled schedulePeriodicAnimationFrame(Runnable runnable) {
        return schedulePulse(runnable, true);
    }

    private Scheduled schedulePulse(Runnable runnable, boolean periodic) {
        return new Scheduled() {
            JavaScriptObject id = programNextPulse();
            boolean cancelled;

            private JavaScriptObject programNextPulse() {
                return id = requestAnimationFrame(this::onPulse);
            }

            private void onPulse() {
                if (!cancelled) {
                    runnable.run();
                    if (periodic)
                        programNextPulse();
                }
            }

            @Override
            public boolean cancel() {
                cancelAnimationFrame(id);
                return cancelled = true;
            }
        };
    }

    private native JavaScriptObject requestAnimationFrame(Runnable runnable) /*-{
        return $wnd.requestAnimationFrame(runnable.@java.lang.Runnable::run().bind(runnable));
    }-*/;

    private native void cancelAnimationFrame(JavaScriptObject id) /*-{
        return $wnd.cancelAnimationFrame(id);
    }-*/;

    private static class GwtScheduled implements Scheduled {
        private final Timer gwtTimer;

        private GwtScheduled(Timer gwtTimer) {
            this.gwtTimer = gwtTimer;
        }

        @Override
        public boolean cancel() {
            gwtTimer.cancel();
            return true;
        }
    }
}
