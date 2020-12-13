package dev.webfx.platform.client.services.uischeduler.spi;

import dev.webfx.platform.shared.services.scheduler.Scheduled;
import dev.webfx.platform.shared.services.scheduler.spi.SchedulerProvider;
import dev.webfx.platform.shared.util.tuples.Unit;
import dev.webfx.platform.client.services.uischeduler.AnimationFramePass;
import java.util.function.Consumer;

/**
 * @author Bruno Salmon
 */
public interface UiSchedulerProvider extends SchedulerProvider {

    boolean isUiThread();

    default void runInUiThread(Runnable runnable) {
        if (isUiThread())
            runnable.run();
        else
            scheduleDeferred(runnable);
    }

    default void runOutUiThread(Runnable runnable) {
        if (!isUiThread())
            runnable.run();
        else
            runInBackground(runnable);
    }

    void requestNextScenePulse();

    boolean isAnimationFrameNow();

    Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask, int afterFrameCount, AnimationFramePass pass);

    // Note: may be in the same animation frame if delay make it possible
    default Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return scheduleDelayInAnimationFrame(delayMs, animationTask, 0, pass);
    }

    // Exclude the possibility for an execution in the current animation frame (if called during an animation frame)
    default Scheduled scheduleDelayInFutureAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return scheduleDelayInAnimationFrame(delayMs, animationTask, isAnimationFrameNow() ? 1 : 0, pass);
    }

    // Same API but with no delay

    default Scheduled scheduleInAnimationFrame(Runnable animationTask, int afterFrameCount, AnimationFramePass pass) {
        return scheduleDelayInAnimationFrame(0, animationTask, afterFrameCount, pass);
    }

    // Note: may be in the same animation frame if possible
    default Scheduled scheduleInAnimationFrame(Runnable animationTask, AnimationFramePass pass) {
        return scheduleDelayInAnimationFrame(0, animationTask, pass);
    }

    // Exclude the possibility for an execution in the current animation frame (if called during an animation frame)
    default Scheduled scheduleInFutureAnimationFrame(Runnable animationTask, AnimationFramePass pass) {
        return scheduleDelayInFutureAnimationFrame(0, animationTask, pass);
    }

    // Correct way of scheduling a property change in the next animation frame
    default Scheduled schedulePropertyChangeInAnimationFrame(Runnable propertyChangeTask) {
        return scheduleInAnimationFrame(propertyChangeTask, AnimationFramePass.PROPERTY_CHANGE_PASS);
    }

    // Periodic API

    // With an initial delay first, then on each animation frame until cancellation
    Scheduled schedulePeriodicInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass);

    // Same but without initial delay
    default Scheduled schedulePeriodicInAnimationFrame(Runnable animationTask, AnimationFramePass pass) {
        return schedulePeriodicInAnimationFrame(0, animationTask, pass);
    }


    // Same Periodic API but with Consumer to make the cancellation easier
    default Scheduled schedulePeriodicInAnimationFrame(long delayMs, Consumer<Scheduled> animationTask, AnimationFramePass pass) {
        Unit<Scheduled> scheduledHolder = new Unit<>();
        Scheduled scheduled = schedulePeriodicInAnimationFrame(delayMs, () -> animationTask.accept(scheduledHolder.get()), pass);
        scheduledHolder.set(scheduled);
        return scheduled;
    }


    default Scheduled schedulePeriodicInAnimationFrame(Consumer<Scheduled> animationTask, AnimationFramePass pass) {
        return schedulePeriodicInAnimationFrame(0, animationTask, pass);
    }


    // Finally repeating the same API but without specifying the animation frame pass (implicitly UI_UPDATE_PASS)

    default Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask, int afterFrameCount) {
        return scheduleDelayInAnimationFrame(delayMs, animationTask, afterFrameCount, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask) {
        return scheduleDelayInAnimationFrame(delayMs, animationTask, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled scheduleDelayInFutureAnimationFrame(long delayMs, Runnable animationTask) {
        return scheduleDelayInFutureAnimationFrame(delayMs, animationTask, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled scheduleInAnimationFrame(Runnable animationTask, int afterFrameCount) {
        return scheduleInAnimationFrame(animationTask, afterFrameCount, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled scheduleInAnimationFrame(Runnable animationTask) {
        return scheduleInAnimationFrame(animationTask, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled scheduleInFutureAnimationFrame(Runnable animationTask) {
        return scheduleInFutureAnimationFrame(animationTask, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled schedulePeriodicInAnimationFrame(long delayMs, Runnable animationTask) {
        return schedulePeriodicInAnimationFrame(delayMs, animationTask, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled schedulePeriodicInAnimationFrame(Runnable animationTask) {
        return schedulePeriodicInAnimationFrame(animationTask, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled schedulePeriodicInAnimationFrame(long delayMs, Consumer<Scheduled> animationTask) {
        return schedulePeriodicInAnimationFrame(delayMs, animationTask, AnimationFramePass.UI_UPDATE_PASS);
    }

    default Scheduled schedulePeriodicInAnimationFrame(Consumer<Scheduled> animationTask) {
        return schedulePeriodicInAnimationFrame(animationTask, AnimationFramePass.UI_UPDATE_PASS);
    }
}
