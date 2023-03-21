package javafx.animation;

import java.util.Arrays;

/**
 * @author Bruno Salmon
 */
abstract class AbstractMasterTimer {

    private static boolean fullspeed = false; // Settings.getBoolean(FULLSPEED_PROP);

    // enables the code path which estimates the next pulse time to be just
    // enough in advance of the vsync to complete rendering before it happens
    private static boolean useAdaptivePulse = false; //Settings.getBoolean(ADAPTIVE_PULSE_PROP);

    private final int PULSE_DURATION_NS = getPulseDuration(1000000000);
    private final int PULSE_DURATION_TICKS = getPulseDuration((int)TickCalculation.fromMillis(1000));

/*
    // This PropertyChangeListener is added to Settings to listen for changes
    // to the nogap and fullspeed properties.
    private static Callback<String, Void> pcl = key -> {
        switch (key) {
            case FULLSPEED_PROP:
                fullspeed = Settings.getBoolean(FULLSPEED_PROP);
                break;
            case ADAPTIVE_PULSE_PROP:
                useAdaptivePulse = Settings.getBoolean(ADAPTIVE_PULSE_PROP);
                break;
            case ANIMATION_MBEAN_ENABLED:
                AnimationPulse.getDefaultBean()
                        .setEnabled(Settings.getBoolean(ANIMATION_MBEAN_ENABLED));
                break;
        }
        return null;
    };
*/

    private boolean paused = false;
    private long totalPausedTime;
    private long startPauseTime;

    // These methods only exist for the sake of testing.
    boolean isPaused() { return paused; }
    long getTotalPausedTime() { return totalPausedTime; }
    long getStartPauseTime() { return startPauseTime; }

    private PulseReceiver receivers[] = new PulseReceiver[2];
    private int receiversLength;
    private boolean receiversLocked;

    // synchronize to update frameJobList and frameJobs
    private TimerReceiver animationTimers[] = new TimerReceiver[2]; // frameJobList
    // snapshot
    private int animationTimersLength;
    private boolean animationTimersLocked;

    // These two variables are ONLY USED if FIXED_PULSE_LENGTH_PROP is true. In this
    // case, instead of advancing time based on the system time (nanos etc) we instead
    // increment each animation by a fixed length of time for each pulse. This is
    // handy while debugging.
    private final long fixedPulseLength = 0; // PULSE_DURATION_NS; // : 0;
    private long debugNanos = 0;

    private final MainLoop theMaster = new MainLoop();


/*
    static {
        Settings.addPropertyChangeListener(pcl);
        int pulse = Settings.getInt(PULSE_PROP, -1);
        if (pulse != -1) {
            System.err.println("Setting PULSE_DURATION to " + pulse + " hz");
        }
    }
*/

    // Used by Clip.create() method that doesn't take a resolution argument
    public int getDefaultResolution() {
        return PULSE_DURATION_TICKS;
    }

    public void pause() {
        if (!paused) {
            startPauseTime = nanos();
            paused = true;
        }
    }

    public void resume() {
        if (paused) {
            paused = false;
            totalPausedTime += nanos() - startPauseTime;
        }
    }

    public long nanos() {
        if (fixedPulseLength > 0)
            return debugNanos;
        return paused ? startPauseTime : System.nanoTime() - totalPausedTime;
    }

    public boolean isFullspeed() {
        return fullspeed;
    }

    /** Prevent external instantiation of MasterTimer. */
    protected AbstractMasterTimer() {
    }

    /**
     * Adds a PulseReceiver to the list of targets being tracked against the
     * global schedule. The target should already have an absolute start time
     * recorded in it and that time will be used to start the clip at the
     * appropriate wall clock time as defined by milliTime().
     *
     * Note that pulseReceiver cannot be removed from the MasterTimer directly.
     * It is removed automatically in the timePulse-iteration if timePulse
     * returns true.
     *
     * @param target
     *            the Clip to be added to the scheduling queue
     */
    public void addPulseReceiver(PulseReceiver target) {
        boolean needMoreSize = receiversLength == receivers.length;
        if (receiversLocked || needMoreSize) {
            receivers = Arrays.copyOf(receivers, needMoreSize ? receivers.length * 3 / 2 + 1 : receivers.length);
            receiversLocked = false;
        }
        receivers[receiversLength++] = target;
        if (receiversLength == 1)
            theMaster.updateAnimationRunnable();
    }

    public void removePulseReceiver(PulseReceiver target) {
        if (receiversLocked) {
            receivers = dev.webfx.platform.util.Arrays.clone(receivers, PulseReceiver[]::new);
            receiversLocked = false;
        }
        for (int i = 0; i < receiversLength; ++i) {
            if (target == receivers[i]) {
                if (i == receiversLength - 1)
                    receivers[i] = null;
                else {
                    System.arraycopy(receivers, i + 1, receivers, i, receiversLength - i - 1);
                    receivers[receiversLength - 1] = null;
                }
                --receiversLength;
                break;
            }
        }
        if (receiversLength == 0)
            theMaster.updateAnimationRunnable();
    }

    public void addAnimationTimer(TimerReceiver timer) {
        boolean needMoreSize = animationTimersLength == animationTimers.length;
        if (animationTimersLocked || needMoreSize) {
            animationTimers = Arrays.copyOf(animationTimers, needMoreSize ? animationTimers.length * 3 / 2 + 1 : animationTimers.length);
            animationTimersLocked = false;
        }
        animationTimers[animationTimersLength++] = timer;
        if (animationTimersLength == 1)
            theMaster.updateAnimationRunnable();
    }

    public void removeAnimationTimer(TimerReceiver timer) {
        if (animationTimersLocked) {
            animationTimers = dev.webfx.platform.util.Arrays.clone(animationTimers, TimerReceiver[]::new);
            animationTimersLocked = false;
        }
        for (int i = 0; i < animationTimersLength; ++i) {
            if (timer == animationTimers[i]) {
                if (i == animationTimersLength - 1)
                    animationTimers[i] = null;
                else {
                    System.arraycopy(animationTimers, i + 1, animationTimers, i, animationTimersLength - i - 1);
                    animationTimers[animationTimersLength - 1] = null;
                }
                --animationTimersLength;
                break;
            }
        }
        if (animationTimersLength == 0) {
            theMaster.updateAnimationRunnable();
        }
    }

    /*
     * methods to record times for different stages of a pulse overriden in
     * MasterTimer to collect data for AnimationPulse Mbean
     */
    protected void recordStart(long shiftMillis) {
    }

    protected void recordEnd() {
    }

    protected void recordAnimationEnd() {
    }

    /**
     * Hidden inner class to run the main timing loop. This is the
     * "AnimationRunnable" for Desktop and TV
     */
    private final class MainLoop implements DelayedRunnable {

        private boolean inactive = true;

        private long nextPulseTime = nanos();
        private long lastPulseDuration = Integer.MIN_VALUE;

        @Override
        public void run() {
            if (paused)
                return;
            scheduled = false;
            long now = nanos();
            recordStart((nextPulseTime - now) / 1000000);
            timePulseImpl(now);
            recordEnd();
            updateNextPulseTime(now);
            // reschedule animation runnable if needed
            updateAnimationRunnable();
        }

        @Override
        public long getDelay() {
            if (fullspeed)
                return 0;
            long now = nanos();
            long timeUntilPulse = (nextPulseTime - now) / 1000000;
            return Math.max(0, timeUntilPulse);
        }

        private void updateNextPulseTime(long pulseStarted) {
            final long now = nanos();
            if (fullspeed)
                nextPulseTime = now;
            else {
                if (useAdaptivePulse) {
                    // Estimate the next pulse time such that we wake up just
                    // early enough to finish up the painting and call swap
                    // before vsync happens. We try to minimize the amount of
                    // time we wait for vsync blocking the EDT thread.
                    nextPulseTime += PULSE_DURATION_NS;
                    long pulseDuration = now - pulseStarted;
                    // if the new duration was smaller than the previous one
                    // we don't need to do anything (we have decreased the
                    // duration), but if it's longer to within 1/2ms then we
                    // try to halve the next anticipated duration (but not
                    // closer
                    // than 2ms within the next expected pulse)
                    if (pulseDuration - lastPulseDuration > 500000)
                        pulseDuration /= 2;
                    if (pulseDuration < 2000000)
                        pulseDuration = 2000000;
                    // if the pulse took longer than pulse_duration_ns we
                    // probably missed the vsync
                    if (pulseDuration >= PULSE_DURATION_NS)
                        pulseDuration = 3 * PULSE_DURATION_NS / 4;
                    lastPulseDuration = pulseDuration;
                    nextPulseTime = nextPulseTime - pulseDuration;
                } else
                    nextPulseTime = ((nextPulseTime + PULSE_DURATION_NS) / PULSE_DURATION_NS)
                            * PULSE_DURATION_NS;
            }
        }

        private boolean scheduled; // Flag used to avoid multiple schedules

        private void updateAnimationRunnable() {
            boolean newInactive = (animationTimersLength == 0 && receiversLength == 0);
            if (inactive != newInactive)
                inactive = newInactive;
            if (!inactive && !scheduled) {
                postUpdateAnimationRunnable(this);
                scheduled = true; // Will be back to false once run() is called
            }
        }
    }

    protected abstract void postUpdateAnimationRunnable(DelayedRunnable animationRunnable);

    protected abstract int getPulseDuration(int precision);

    protected void timePulseImpl(long now) {
        if (fixedPulseLength > 0) {
            debugNanos += fixedPulseLength;
            now = debugNanos;
        }
        PulseReceiver receiversSnapshot[] = receivers;
        int rLength = receiversLength;
        try {
            receiversLocked = true;
            for (int i = 0; i < rLength; i++)
                receiversSnapshot[i].timePulse(TickCalculation.fromNano(now));
        } finally {
            receiversLocked = false;
        }
        recordAnimationEnd();

        TimerReceiver animationTimersSnapshot[] = animationTimers;
        int aTLength = animationTimersLength;
        try {
            animationTimersLocked = true;
            // After every frame, call any frame jobs
            for (int i = 0; i < aTLength; i++)
                animationTimersSnapshot[i].handle(now);
        } finally {
            animationTimersLocked = false;
        }
    }
}
