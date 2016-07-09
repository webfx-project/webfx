package naga.providers.toolkit.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import naga.commons.scheduler.Scheduled;
import naga.commons.scheduler.Scheduler;

/**
 * @author Bruno Salmon
 */
class FxScheduler implements Scheduler {

    public static FxScheduler SINGLETON = new FxScheduler();

    private FxScheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Override
    public FxScheduled scheduleDelay(long delayMs, Runnable runnable) {
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
        return Platform.isFxApplicationThread();
    }

    private static class FxScheduled implements Scheduled {
        private final Timeline timeline;

        private FxScheduled(Timeline timeline) {
            this.timeline = timeline;
        }

        @Override
        public boolean cancel() {
            timeline.stop();
            return true;
        }
    }
}
