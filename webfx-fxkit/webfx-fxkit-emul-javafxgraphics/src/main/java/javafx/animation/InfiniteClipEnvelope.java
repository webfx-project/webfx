package javafx.animation;

import javafx.util.Duration;

/**
 * @author Bruno Salmon
 */
public class InfiniteClipEnvelope extends ClipEnvelope {

    private boolean autoReverse;
    private long pos;

    protected InfiniteClipEnvelope(Animation animation) {
        super(animation);
        if (animation != null) {
            autoReverse = animation.isAutoReverse();
        }
    }

    @Override
    public void setAutoReverse(boolean autoReverse) {
        this.autoReverse = autoReverse;
    }

    @Override
    protected double calculateCurrentRate() {
        return !autoReverse? rate
                : (ticks % (2 * cycleTicks) < cycleTicks)? rate : -rate;
    }

    @Override
    public ClipEnvelope setCycleDuration(Duration cycleDuration) {
        if (cycleDuration == Animation.INDEFINITE_DURATION) {
            return create(animation);
        }
        updateCycleTicks(cycleDuration);
        return this;
    }

    @Override
    public ClipEnvelope setCycleCount(int cycleCount) {
        return (cycleCount != Animation.INDEFINITE)? create(animation) : this;
    }

    @Override
    public void setRate(double rate) {
        final Animation.Status status = animation.getStatus();
        if (status != Animation.Status.STOPPED) {
            if (status == Animation.Status.RUNNING) {
                setCurrentRate((Math.abs(currentRate - this.rate) < EPSILON) ? rate : -rate);
            }
            deltaTicks = ticks - Math.round((ticks - deltaTicks) * Math.abs(rate / this.rate));
            if (rate * this.rate < 0) {
                final long delta = 2 * cycleTicks - pos;
                deltaTicks += delta;
                ticks += delta;
            }
            abortCurrentPulse();
        }
        this.rate = rate;
    }

    @Override
    public void timePulse(long currentTick) {
        if (cycleTicks == 0L) {
            return;
        }
        aborted = false;
        inTimePulse = true;

        try {
            final long oldTicks = ticks;
            ticks = Math.max(0, deltaTicks + Math.round(currentTick * Math.abs(rate)));

            long overallDelta = ticks - oldTicks; // overall delta between current position and new position
            if (overallDelta == 0) {
                return;
            }

            long cycleDelta = (currentRate > 0)? cycleTicks - pos : pos; // delta to reach end of cycle

            while (overallDelta >= cycleDelta) {
                if (cycleDelta > 0) {
                    pos = (currentRate > 0)? cycleTicks : 0;
                    overallDelta -= cycleDelta;
                    AnimationAccessor.getDefault().playTo(animation, pos, cycleTicks);
                    if (aborted) {
                        return;
                    }
                }
                if (autoReverse) {
                    setCurrentRate(-currentRate);
                } else {
                    pos = (currentRate > 0)? 0 : cycleTicks;
                    AnimationAccessor.getDefault().jumpTo(animation, pos, cycleTicks, false);
                }
                cycleDelta = cycleTicks;
            }

            if (overallDelta > 0) {
                pos += (currentRate > 0)? overallDelta : -overallDelta;
                AnimationAccessor.getDefault().playTo(animation, pos, cycleTicks);
            }

        } finally {
            inTimePulse = false;
        }
    }

    @Override
    public void jumpTo(long newTicks) {
        if (cycleTicks == 0L) {
            return;
        }
        final long oldTicks = ticks;
        ticks = Math.max(0, newTicks) % (2 * cycleTicks);
        final long delta = ticks - oldTicks;
        if (delta != 0) {
            deltaTicks += delta;
            if (autoReverse) {
                if (ticks > cycleTicks) {
                    pos = 2 * cycleTicks - ticks;
                    if (animation.getStatus() == Animation.Status.RUNNING) {
                        setCurrentRate(-rate);
                    }
                } else {
                    pos = ticks;
                    if (animation.getStatus() == Animation.Status.RUNNING) {
                        setCurrentRate(rate);
                    }
                }
            } else {
                pos = ticks % cycleTicks;
                if (pos == 0) {
                    pos = ticks;
                }
            }
            AnimationAccessor.getDefault().jumpTo(animation, pos, cycleTicks, false);
            abortCurrentPulse();
        }
    }

}
