package naga.uischeduler.impl;

import naga.uischeduler.AnimationFramePass;
import naga.scheduler.Scheduled;
import naga.uischeduler.UiScheduler;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Bruno Salmon
 */
public abstract class UiSchedulerBase implements UiScheduler {

    @Override
    public void scheduleDeferred(Runnable runnable) {
        scheduleInNextAnimationFrame(runnable, AnimationFramePass.UI_UPDATE_PASS);
    }

    @Override
    public Scheduled scheduleDelay(long delayMs, Runnable runnable) {
        return scheduleDelayInAnimationFrame(delayMs, runnable, AnimationFramePass.UI_UPDATE_PASS);
    }

    @Override
    public Scheduled scheduleDelayInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return scheduleInAnimationFrame(delayMs, animationTask, pass, false, 0);
    }

    @Override
    public Scheduled scheduleInFutureAnimationFrame(int frameCount, Runnable animationTask, AnimationFramePass pass) {
        return scheduleInAnimationFrame(0, animationTask, pass, false, frameCount);
    }

    @Override
    public Scheduled schedulePeriodic(long delayMs, Runnable runnable) {
        return schedulePeriodicInAnimationFrame(delayMs, runnable, AnimationFramePass.UI_UPDATE_PASS);
    }

    @Override
    public Scheduled schedulePeriodicInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass) {
        return scheduleInAnimationFrame(delayMs, animationTask, pass, true, 0);
    }

    private Scheduled scheduleInAnimationFrame(long delayMs, Runnable animationTask, AnimationFramePass pass, boolean periodic, int postponeFrameCount) {
        return new AnimationScheduled(delayMs, animationTask, pass, periodic, postponeFrameCount);
    }

    @Override
    public void requestNextScenePulse() {
        checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();
    }

    public class AnimationScheduled implements Scheduled {
        private final Runnable runnable;
        private final long delayMs;
        private final boolean periodic;
        private int futureFrameCount;
        private long nextExecutionTime;
        private boolean cancelled;

        private AnimationScheduled(long delayMs, Runnable runnable, AnimationFramePass pass, boolean periodic, int futureFrameCount) {
            this.runnable = runnable;
            this.delayMs = delayMs;
            this.periodic = periodic;
            this.futureFrameCount = futureFrameCount;
            nextExecutionTime = delayMs == 0 ? 0 : System.currentTimeMillis() + delayMs;
            switch (pass) {
                case UI_UPDATE_PASS: uiAnimations.add(this); break;
                case PROPERTY_CHANGE_PASS: propertiesAnimations.add(this); break;
                case SCENE_PULSE_LAYOUT_PASS: pulseAnimations.add(this); break;
            }
            checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();
        }

        @Override
        public boolean cancel() {
            return cancelled = true; // will be removed on next animation frame
        }

        private boolean shouldExecuteNow() {
            if (futureFrameCount > 0) {
                futureFrameCount--;
                return false;
            }
            return nextExecutionTime == 0 || System.currentTimeMillis() >= nextExecutionTime;
        }

        private void execute() {
            runnable.run();
            if (nextExecutionTime != 0 && periodic)
                nextExecutionTime += delayMs;
        }
    }

    protected abstract void checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();

    protected void onExecuteAnimationPipeFinished(boolean noMoreAnimationScheduled) {
    }

    private final List<AnimationScheduled> uiAnimations = new ArrayList<>();
    private final List<AnimationScheduled> propertiesAnimations = new ArrayList<>();
    private final List<AnimationScheduled> pulseAnimations = new ArrayList<>();

    private final static long MILLIS_IN_NANO = 1_000_000;
    private final static long SECOND_IN_NANO = 1_000 * MILLIS_IN_NANO;
    private final static long MAX_ANIMATION_FRAME_DURATION_NANO = SECOND_IN_NANO / 60;

    private final ThreadLocal<Boolean> animationFrame = new ThreadLocal<>();
    @Override
    public boolean isAnimationFrameNow() {
        return animationFrame.get() == Boolean.TRUE;
    }

    protected void executeAnimationPipe() {
        animationFrame.set(Boolean.TRUE);
        long uiPassStart = nanoTime();
        executeAnimations(uiAnimations);
        long propertiesPassStart = nanoTime();
        executeAnimations(propertiesAnimations);
        long pulsePassStart = nanoTime();
        executeAnimations(pulseAnimations);
        long nanoEnd = nanoTime();
        long animationDuration = nanoEnd - uiPassStart;
        animationFrame.set(Boolean.FALSE);
        if (animationDuration > MAX_ANIMATION_FRAME_DURATION_NANO)
            log("Long animation: " + animationDuration / MILLIS_IN_NANO + "ms (60 FPS = 16ms) = "
                    + (propertiesPassStart - uiPassStart) / MILLIS_IN_NANO + "ms ui + " +
                    + (pulsePassStart - propertiesPassStart) / MILLIS_IN_NANO + "ms properties + "
                    + (nanoEnd - pulsePassStart) / MILLIS_IN_NANO  + "ms layout/pulse");
        boolean noMoreAnimationScheduled = propertiesAnimations.isEmpty() && uiAnimations.isEmpty();
        onExecuteAnimationPipeFinished(noMoreAnimationScheduled);
        if (!noMoreAnimationScheduled)
            checkExecuteAnimationPipeIsScheduledForNextAnimationFrame();
    }

    protected void log(String message) {
        System.out.println(message);
    }

    private void executeAnimations(List<AnimationScheduled> animations) {
        for (int i = 0; i < animations.size(); i++) {
            AnimationScheduled scheduled = animations.get(i);
            if (scheduled.cancelled)
                animations.remove(i--);
            else if (scheduled.shouldExecuteNow()) {
                scheduled.execute();
                if (!scheduled.periodic || scheduled.cancelled)
                    animations.remove(i--);
            }
        }
    }

    public void runLikeInAnimationFrame(Runnable runnable) {
        Boolean af = animationFrame.get();
        animationFrame.set(Boolean.TRUE);
        runnable.run();
        animationFrame.set(af);
    }
}
