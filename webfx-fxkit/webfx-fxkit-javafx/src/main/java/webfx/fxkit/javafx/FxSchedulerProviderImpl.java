package webfx.fxkit.javafx;

import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import webfx.fxkits.core.spi.FxKit;
import webfx.platforms.core.services.uischeduler.spi.impl.UiSchedulerProviderImplBase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bruno Salmon
 */
public class FxSchedulerProviderImpl extends UiSchedulerProviderImplBase {

    static FxSchedulerProviderImpl SINGLETON = new FxSchedulerProviderImpl();
    private final ExecutorService executor = Executors.newCachedThreadPool();

    public FxSchedulerProviderImpl() {
    }

    @Override
    public void runInBackground(Runnable runnable) {
        executor.execute(runnable);
    }

    @Override
    public void scheduleDeferred(Runnable runnable) {
        FxKit fxKit = FxKit.get();
        if (!fxKit.isReady())
            fxKit.onReady(runnable);
        else
            Platform.runLater(runnable);
    }

    @Override
    public boolean isUiThread() {
        return FxKit.get().isReady() && Platform.isFxApplicationThread();
    }

    @Override
    public long nanoTime() {
        return System.nanoTime();
    }

    private Timeline timeline;

    @Override
    protected void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame() {
        synchronized (this) {
            if (timeline == null) {
                timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> this.executeAnimationPipe()));
                timeline.setCycleCount(Integer.MAX_VALUE);
                timeline.play();
            }
        }
    }

    @Override
    protected void onExecuteAnimationPipeFinished(boolean noMoreAnimationScheduled) {
        synchronized (this) {
            if (noMoreAnimationScheduled && timeline != null) {
                timeline.stop();
                timeline = null;
            }
        }
    }
}
