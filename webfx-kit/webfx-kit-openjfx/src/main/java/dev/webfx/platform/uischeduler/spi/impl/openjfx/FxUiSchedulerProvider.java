package dev.webfx.platform.uischeduler.spi.impl.openjfx;

import dev.webfx.platform.scheduler.spi.SchedulerProviderBase;
import javafx.animation.Animation;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.util.Duration;
import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.platform.uischeduler.spi.impl.UiSchedulerProviderBase;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @author Bruno Salmon
 */
public final class FxUiSchedulerProvider extends UiSchedulerProviderBase {

    private final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    protected SchedulerProviderBase.ScheduledBase scheduledImpl(SchedulerProviderBase.WrappedRunnable wrappedRunnable, long delayMs) {
        // Redirecting to specific Java or JavaFX API in the following cases:
        // 1) Background tasks can be executed in parallel to the UI thread using the Java thread pool executor
        if (wrappedRunnable.isBackground()) {
            executor.execute(wrappedRunnable);
        }
        // 2) Deferred tasks with no delay can be sent to JavaFX Platform.runLater()
        else if (wrappedRunnable.isDeferred() && delayMs == 0) {
            if (!WebFxKitLauncher.isReady())
                WebFxKitLauncher.onReady(wrappedRunnable);
            else
                Platform.runLater(wrappedRunnable);
        }
        // Everything else can use the default base UI implementation (the task will be executed in a future animation frame)
        else
            return super.scheduledImpl(wrappedRunnable, delayMs);
        // For both cases 1) and 2), the Java/JavaFX API used is returning void, so there is no further specific action
        // to cancel the task than the one provided by default in the base implementation (i.e. the wrapped runnable will
        // just skip the call to the task runnable when it will be executed).
        return new ScheduledBase(wrappedRunnable);
    }

    @Override
    public boolean isUiThread() {
        return WebFxKitLauncher.isReady() && Platform.isFxApplicationThread();
    }

    private Timeline timeline;

    @Override
    protected void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame() {
        synchronized (this) {
            if (timeline == null) {
                timeline = new Timeline(new KeyFrame(Duration.millis(1), event -> this.executeAnimationPipe()));
                timeline.setCycleCount(Animation.INDEFINITE);
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
