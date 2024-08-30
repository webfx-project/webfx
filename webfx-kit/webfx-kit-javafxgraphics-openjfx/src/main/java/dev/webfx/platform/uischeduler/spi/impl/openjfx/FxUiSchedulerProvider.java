package dev.webfx.platform.uischeduler.spi.impl.openjfx;

import dev.webfx.kit.launcher.WebFxKitLauncher;
import dev.webfx.platform.scheduler.spi.SchedulerProviderBase;
import dev.webfx.platform.uischeduler.spi.impl.UiSchedulerProviderBase;
import javafx.animation.AnimationTimer;
import javafx.application.Platform;

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

    private Runnable animationRunnable;
    private final AnimationTimer animationTimer = new AnimationTimer() {
        @Override
        public void handle(long now) {
            animationRunnable.run();
        }
    };

    @Override
    protected boolean isSystemAnimationFrameRunning() {
        // As opposed to the browser, OpenJFX never stops running animation frames
        return true;
    }

    @Override
    protected void requestAnimationFrame(Runnable runnable) {
        animationRunnable = runnable;
        animationTimer.start();
    }

    @Override
    protected void cancelAnimationFrame() {
        animationTimer.stop();
    }
}
