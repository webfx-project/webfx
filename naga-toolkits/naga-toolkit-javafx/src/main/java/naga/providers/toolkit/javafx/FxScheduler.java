package naga.providers.toolkit.javafx;

import com.sun.javafx.tk.TKPulseListener;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import naga.commons.scheduler.Scheduled;
import naga.commons.scheduler.Scheduler;
import naga.commons.util.function.Callable;
import naga.toolkit.spi.Toolkit;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
class FxScheduler implements Scheduler {

    static FxScheduler SINGLETON = new FxScheduler();

    private FxScheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        Toolkit toolkit = Toolkit.get();
        if (!toolkit.isReady())
            toolkit.onReady(runnable);
        else
            Platform.runLater(runnable);
    }

    @Override
    public FxScheduled scheduleDelay(long delayMs, Runnable runnable) {
        if (delayMs <= 0) { // KeyFrame doesn't work in this case
            scheduleDeferred(runnable);
            return new FxScheduled(null);
        }
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delayMs), event -> runnable.run()));
        timeline.setCycleCount(1);
        timeline.play();
        return new FxScheduled(timeline);
    }

    @Override
    public FxScheduled schedulePeriodic(long delayMs, Runnable runnable) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delayMs), event -> runnable.run()));
        timeline.setCycleCount(Integer.MAX_VALUE);
        timeline.play();
        return new FxScheduled(timeline);
    }

    @Override
    public boolean isUiThread() {
        return Toolkit.get().isReady() && Platform.isFxApplicationThread();
    }

    private List<Runnable> pendingAnimationFrameRunnables;
    private Scheduled scheduledPulse;
    @Override
    public Scheduled scheduleAnimationFrame(long delayMs, Runnable runnable) {
        if (delayMs > 0)
            return scheduleDelay(delayMs, runnable);
        if (scheduledPulse == null) {
            scheduledPulse = schedulePeriodicAnimationFrame(() -> {
                List<Runnable> runnables = pendingAnimationFrameRunnables;
                if (runnables == null) {
                    scheduledPulse.cancel();
                    scheduledPulse = null;
                } else {
                    pendingAnimationFrameRunnables = null;
                    for (Runnable r : runnables)
                        r.run();
                }
            });
        }
        if (pendingAnimationFrameRunnables == null)
            pendingAnimationFrameRunnables = new ArrayList<>();
        pendingAnimationFrameRunnables.add(runnable);
        return null;
    }

    @Override
    public Scheduled schedulePeriodicAnimationFrame(Runnable runnable) {
        return predicateScheduled(Toolkit.get()::isApplicationWindowCreated, () -> readySchedulePeriodicPulse(runnable));
    }

    private Scheduled readySchedulePeriodicPulse(Runnable runnable) {
        TKPulseListener listener = runnable::run;
        com.sun.javafx.tk.Toolkit.getToolkit().addSceneTkPulseListener(listener);
        return () -> {
            com.sun.javafx.tk.Toolkit.getToolkit().removeSceneTkPulseListener(listener);
            return true;
        };
    }


    private Scheduled predicateScheduled(Callable<Boolean> readyTest, Callable<Scheduled> readyScheduler) {
        return readyTest.call() ? readyScheduler.call() : new Scheduled() {
            Scheduled finalScheduled;
            {
                scheduleDeferred(new Runnable() {
                    @Override
                    public void run() {
                        if (!readyTest.call())
                            scheduleDeferred(this);
                        else
                            finalScheduled = readyScheduler.call();
                    }
                });
            }

            @Override
            public boolean cancel() {
                return finalScheduled != null && finalScheduled.cancel();
            }
        };
    }

    private static class FxScheduled implements Scheduled {
        private final Timeline timeline;

        private FxScheduled(Timeline timeline) {
            this.timeline = timeline;
        }

        @Override
        public boolean cancel() {
            if (timeline != null)
                timeline.stop();
            return true;
        }
    }
}
