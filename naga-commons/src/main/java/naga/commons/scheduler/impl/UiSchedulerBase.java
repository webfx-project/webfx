package naga.commons.scheduler.impl;

import naga.commons.scheduler.Scheduled;
import naga.commons.scheduler.UiScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class UiSchedulerBase implements UiScheduler {

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleAnimationFrame(runnable);
    }

    @Override
    public Scheduled scheduleDelay(long delayMs, Runnable runnable) {
        return scheduleAnimationFrame(delayMs, runnable);
    }

    @Override
    public Scheduled schedulePeriodic(long delayMs, Runnable runnable) {
        return new AnimationScheduled(delayMs, runnable, true, false);
    }

    @Override
    public Scheduled scheduleAnimationFrame(long delayMs, Runnable runnable) {
        return new AnimationScheduled(delayMs, runnable, false, false);
    }

    @Override
    public Scheduled schedulePeriodicAnimationFrame(Runnable runnable, boolean isPulse) {
        return new AnimationScheduled(runnable, true, isPulse);
    }

    @Override
    public void requestNextPulse() {
        checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();
    }

    public class AnimationScheduled implements Scheduled {
        private final Runnable runnable;
        private final long delayMs;
        private final boolean periodic;
        private long nextExecutionTime;
        private boolean cancelled;

        private AnimationScheduled(Runnable runnable) {
            this(runnable, false, false);
        }

        private AnimationScheduled(Runnable runnable, boolean periodic, boolean isPulse) {
            this(0, runnable, periodic, isPulse);
        }

        public AnimationScheduled(long delayMs, Runnable runnable, boolean periodic, boolean isPulse) {
            this.runnable = runnable;
            this.delayMs = delayMs;
            this.periodic = periodic;
            nextExecutionTime = delayMs == 0 ? 0 : System.currentTimeMillis() + delayMs;
            (isPulse ? pulseAnimations : generalAnimations).add(this);
            checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();
        }

        @Override
        public boolean cancel() {
            return cancelled = true; // will be removed on next animation frame
        }

        private boolean isCancelled() {
            return cancelled;
        }

        private boolean isPeriodic() {
            return periodic;
        }

        private boolean shouldExecuteNow() {
            return nextExecutionTime == 0 || System.currentTimeMillis() >= nextExecutionTime;
        }

        private void execute() {
            runnable.run();
            if (nextExecutionTime !=0 && periodic)
                nextExecutionTime += delayMs;
        }
    }

    private final List<AnimationScheduled> generalAnimations = new ArrayList<>();
    private final List<AnimationScheduled> pulseAnimations = new ArrayList<>();

    protected abstract void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();

    protected void onExecuteAnimationPipeFinished(boolean noMoreAnimationScheduled) {
    }

    private boolean animationFrame;
    @Override
    public boolean isAnimationFrame() {
        return animationFrame;
    }

    protected void executeAnimationPipe() {
        animationFrame = true;
        executeAnimations(generalAnimations);
        executeAnimations(pulseAnimations);
        animationFrame = false;
        boolean noMoreAnimationScheduled = generalAnimations.isEmpty();
        onExecuteAnimationPipeFinished(noMoreAnimationScheduled);
        if (!noMoreAnimationScheduled)
            checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();
    }

    private void executeAnimations(List<AnimationScheduled> animations) {
        for (int i = 0; i < animations.size(); i++) {
            AnimationScheduled scheduled = animations.get(i);
            if (scheduled.isCancelled())
                animations.remove(i--);
            else if (scheduled.shouldExecuteNow()) {
                scheduled.execute();
                if (!scheduled.isPeriodic())
                    animations.remove(i--);
            }
        }
    }

    public void runLikeAnimationFrame(Runnable runnable) {
        boolean af = animationFrame;
        animationFrame = true;
        runnable.run();
        animationFrame = af;
    }
}

