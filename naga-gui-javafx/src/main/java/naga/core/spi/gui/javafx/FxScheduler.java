package naga.core.spi.gui.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import naga.core.spi.platform.Scheduler;

/**
 * @author Bruno Salmon
 */
class FxScheduler implements Scheduler<Timeline> {

    public static FxScheduler SINGLETON = new FxScheduler();

    private FxScheduler() {
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        Platform.runLater(runnable);
    }

    @Override
    public Timeline scheduleDelay(long delayMs, Runnable runnable) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delayMs), event -> runnable.run()));
        timeline.setCycleCount(1);
        timeline.play();
        return timeline;
    }

    @Override
    public Timeline schedulePeriodic(long delayMs, Runnable runnable) {
        Timeline timeline = new Timeline(new KeyFrame(Duration.millis(delayMs), event -> runnable.run()));
        timeline.setCycleCount(Integer.MAX_VALUE);
        timeline.play();
        return timeline;
    }

    @Override
    public boolean cancelTimer(Timeline timeline) {
        timeline.stop();
        return true;
    }

    @Override
    public boolean isUiThread() {
        return Platform.isFxApplicationThread();
    }
}
