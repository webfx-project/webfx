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
        return new AnimationFrameScheduled(delayMs, runnable, true);
    }

    @Override
    public Scheduled scheduleAnimationFrame(long delayMs, Runnable runnable) {
        return new AnimationFrameScheduled(delayMs, runnable, false);
    }

    @Override
    public Scheduled schedulePeriodicAnimationFrame(Runnable runnable) {
        return new AnimationFrameScheduled(runnable, true);
    }

    public class AnimationFrameScheduled implements Scheduled {
        private final Runnable runnable;
        private final long delayMs;
        private final boolean periodic;
        private long nextExecutionTime;
        private boolean cancelled;

        private AnimationFrameScheduled(Runnable runnable) {
            this(runnable,false);
        }

        private AnimationFrameScheduled(Runnable runnable, boolean periodic) {
            this(0, runnable, periodic);
        }

        public AnimationFrameScheduled(long delayMs, Runnable runnable, boolean periodic) {
            this.runnable = runnable;
            this.delayMs = delayMs;
            this.periodic = periodic;
            nextExecutionTime = delayMs == 0 ? 0 : System.currentTimeMillis() + delayMs;
            animations.add(this);
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

    private final List<AnimationFrameScheduled> animations = new ArrayList<>();

    protected abstract void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();

    protected void onExecuteAnimationPipeFinished(boolean noMoreAnimationScheduled) {
    }

    protected void executeAnimationPipe() {
        for (int i = 0; i < animations.size(); i++) {
            AnimationFrameScheduled scheduled = animations.get(i);
            if (scheduled.isCancelled())
                animations.remove(i--);
            else if (scheduled.shouldExecuteNow()) {
                scheduled.execute();
                if (!scheduled.isPeriodic())
                    animations.remove(i--);
            }
        }
        boolean noMoreAnimationScheduled = animations.isEmpty();
        onExecuteAnimationPipeFinished(noMoreAnimationScheduled);
        if (!noMoreAnimationScheduled)
            checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();
    }
}

