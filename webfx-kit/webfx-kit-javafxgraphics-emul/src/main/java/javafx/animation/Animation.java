package javafx.animation;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.util.Duration;
import javafx.beans.property.*;
import javafx.collections.FXCollections;
import javafx.collections.ObservableMap;

import java.util.HashMap;

import static javafx.animation.TickCalculation.fromDuration;

/**
 * The class {@code Animation} provides the core functionality of all animations
 * used in the JavaFX runtime.
 * <p>
 * An animation can run in a loop by setting {@link #cycleCount}. To make an
 * animation run back and forth while looping, set the {@link #autoReverse}
 * -flag.
 * <p>
 * Call {@link #play()} or {@link #playFromStart()} to play an {@code Animation}
 * . The {@code Animation} progresses in the direction and speed specified by
 * {@link #rate}, and stops when its duration is elapsed. An {@code Animation}
 * with indefinite duration (a {@link #cycleCount} of {@link #INDEFINITE}) runs
 * repeatedly until the {@link #stop()} method is explicitly called, which will
 * stop the running {@code Animation} and reset its play head to the initial
 * position.
 * <p>
 * An {@code Animation} can be paused by calling {@link #pause()}, and the next
 * {@link #play()} call will resume the {@code Animation} from where it was
 * paused.
 * <p>
 * An {@code Animation}'s play head can be randomly positioned, whether it is
 * running or not. If the {@code Animation} is running, the play head jumps to
 * the specified position immediately and continues playing from new position.
 * If the {@code Animation} is not running, the next {@link #play()} will start
 * the {@code Animation} from the specified position.
 * <p>
 * Inverting the value of {@link #rate} toggles the play direction.
 *
 * @see Timeline
 * @see Transition
 */
public abstract class Animation {

    static {
        AnimationAccessor.DEFAULT = new AnimationAccessorImpl();
    }

    /**
     * Used to specify an animation that repeats indefinitely, until the
     * {@code stop()} method is called.
     */
    public static final int INDEFINITE = -1;
    public static final Duration INDEFINITE_DURATION = Duration.millis(Long.MAX_VALUE);

    /**
     * The possible states for {@link Animation#statusProperty status}.
     * @since JavaFX 2.0
     */
    public enum Status {
        /**
         * The paused state.
         */
        PAUSED,
        /**
         * The running state.
         */
        RUNNING,
        /**
         * The stopped state.
         */
        STOPPED
    }

    private static final double EPSILON = 1e-12;

    /*
        These four fields and associated methods were moved here from AnimationPulseReceiver
        when that class was removed. They could probably be integrated much cleaner into Animation,
        but to make sure the change was made without introducing regressions, this code was
        moved pretty much verbatim.
     */
    private long startTime;
    private long pauseTime;
    private boolean paused = false;
    private final AbstractMasterTimer timer;

    // Access control context, captured whenever we add this pulse reciever to
    // the master timer (which is called when an animation is played or resumed)
    //private AccessControlContext accessCtrlCtx = null;

    private long now() {
        return TickCalculation.fromNano(timer.nanos());
    }

    private void addPulseReceiver() {
        // Capture the Access Control Context to be used during the animation pulse
        //accessCtrlCtx = AccessController.getContext();

        timer.addPulseReceiver(pulseReceiver);
    }

    void startReceiver(long delay) {
        paused = false;
        startTime = now() + delay;
        addPulseReceiver();
    }

    void pauseReceiver() {
        if (!paused) {
            pauseTime = now();
            paused = true;
            timer.removePulseReceiver(pulseReceiver);
        }
    }

    void resumeReceiver() {
        if (paused) {
            final long deltaTime = now() - pauseTime;
            startTime += deltaTime;
            paused = false;
            addPulseReceiver();
        }
    }

    // package private only for the sake of testing
    final PulseReceiver pulseReceiver = new PulseReceiver() {
        @Override public void timePulse(long now) {
            final long elapsedTime = now - startTime;
            if (elapsedTime < 0)
                return;
/*
            if (accessCtrlCtx == null) {
                throw new IllegalStateException("Error: AccessControlContext not captured");
            }
*/

            //AccessController.doPrivileged((PrivilegedAction<Void>) () -> {
                impl_timePulse(elapsedTime);
            //    return null;
            //}, accessCtrlCtx);
        }
    };

    private class CurrentRateProperty extends SimpleDoubleProperty {

        public CurrentRateProperty() {
            super(Animation.this, "currentRate");
        }

        public void set(double value) {
            super.set(value);
            fireValueChangedEvent();
        }
    }

    private class AnimationReadOnlyProperty<T> extends SimpleObjectProperty<T> {

        private AnimationReadOnlyProperty(String name, T value) {
            super(Animation.this, name, value);
        }

        public void set(T value) {
            super.set(value);
            fireValueChangedEvent();
        }
    }

    /**
     * The parent of this {@code Animation}. If this animation has not been
     * added to another animation, such as {@link ParallelTransition} and
     * {@link SequentialTransition}, then parent will be null.
     */
    Animation parent = null;

    /* Package-private for testing purposes */
    ClipEnvelope clipEnvelope;

    private boolean lastPlayedFinished = false;

    private boolean lastPlayedForward = true;
    /**
     * Defines the direction/speed at which the {@code Animation} is expected to
     * be played.
     * <p>
     * The absolute value of {@code rate} indicates the speed which the
     * {@code Animation} is to be played, while the sign of {@code rate}
     * indicates the direction. A positive value of {@code rate} indicates
     * forward play, a negative value indicates backward play and {@code 0.0} to
     * stop a running {@code Animation}.
     * <p>
     * Rate {@code 1.0} is normal play, {@code 2.0} is 2 time normal,
     * {@code -1.0} is backwards, etc...
     *
     * <p>
     * Inverting the rate of a running {@code Animation} will cause the
     * {@code Animation} to reverse direction in place and play back over the
     * portion of the {@code Animation} that has already elapsed.
     */
    private DoubleProperty rate;
    private static final double DEFAULT_RATE = 1.0;

    public final void setRate(double value) {
        if ((rate != null) || (Math.abs(value - DEFAULT_RATE) > EPSILON))
            rateProperty().setValue(value);
    }

    public final double getRate() {
        return (rate == null)? DEFAULT_RATE : rate.getValue();
    }

    public final DoubleProperty rateProperty() {
        if (rate == null)
            rate = new SimpleDoubleProperty(DEFAULT_RATE) {

                @Override
                public void invalidated() {
                    final double newRate = getRate();
                    if (isRunningEmbedded()) {
                        if (isBound())
                            unbind();
                        set(oldRate);
                        throw new IllegalArgumentException("Cannot set rate of embedded animation while running.");
                    } else {
                        if (Math.abs(newRate) < EPSILON) {
                            if (getStatus() == Status.RUNNING)
                                lastPlayedForward = (Math.abs(getCurrentRate()
                                        - oldRate) < EPSILON);
                            setCurrentRate(0.0);
                            pauseReceiver();
                        } else {
                            if (getStatus() == Status.RUNNING) {
                                double currentRate = getCurrentRate();
                                if (Math.abs(currentRate) < EPSILON) {
                                    setCurrentRate(lastPlayedForward ? newRate : -newRate);
                                    resumeReceiver();
                                } else {
                                    boolean playingForward = Math.abs(currentRate - oldRate) < EPSILON;
                                    setCurrentRate(playingForward ? newRate : -newRate);
                                }
                            }
                            oldRate = newRate;
                        }
                        clipEnvelope.setRate(newRate);
                    }
                }

                @Override
                public Object getBean() {
                    return Animation.this;
                }

                @Override
                public String getName() {
                    return "rate";
                }
            };
        return rate;
    }

    private boolean isRunningEmbedded() {
        return parent != null && (parent.getStatus() != Status.STOPPED || parent.isRunningEmbedded());
    }

    private double oldRate = 1.0;
    /**
     * Read-only variable to indicate current direction/speed at which the
     * {@code Animation} is being played.
     * <p>
     * {@code currentRate} is not necessary equal to {@code rate}.
     * {@code currentRate} is set to {@code 0.0} when animation is paused or
     * stopped. {@code currentRate} may also point to different direction during
     * reverse cycles when {@code autoReverse} is {@code true}
     */
    private ReadOnlyDoubleProperty currentRate;
    private static final double DEFAULT_CURRENT_RATE = 0.0;

    private void setCurrentRate(double value) {
        if ((currentRate != null) || (Math.abs(value - DEFAULT_CURRENT_RATE) > EPSILON))
            ((CurrentRateProperty)currentRateProperty()).set(value);
    }

    public final Double getCurrentRate() {
        return (currentRate == null)? DEFAULT_CURRENT_RATE : currentRate.getValue();
    }

    public final ReadOnlyDoubleProperty currentRateProperty() {
        if (currentRate == null)
            currentRate = new CurrentRateProperty();
        return currentRate;
    }

    /**
     * Read-only variable to indicate the duration of one cycle of this
     * {@code Animation}: the time it takes to play from time 0 to the
     * end of the Animation (at the default {@code rate} of
     * 1.0).
     */
    private ReadOnlyObjectProperty<Duration> cycleDuration;
    private static final Duration DEFAULT_CYCLE_DURATION = Duration.ZERO;

    protected final void setCycleDuration(Duration value) {
        if ((cycleDuration != null) || (!DEFAULT_CYCLE_DURATION.equals(value))) {
            if (value.lessThan(Duration.ZERO))
                throw new IllegalArgumentException("Cycle duration cannot be negative");
            ((AnimationReadOnlyProperty<Duration>)cycleDurationProperty()).set(value);
            updateTotalDuration();
        }
    }

    public final Duration getCycleDuration() {
        return (cycleDuration == null)? DEFAULT_CYCLE_DURATION : cycleDuration.get();
    }

    public final ReadOnlyObjectProperty<Duration> cycleDurationProperty() {
        if (cycleDuration == null)
            cycleDuration = new AnimationReadOnlyProperty<>("cycleDuration", DEFAULT_CYCLE_DURATION);
        return cycleDuration;
    }

    /**
     * Read-only variable to indicate the total duration of this
     * {@code Animation}, including repeats. A {@code Animation} with a {@code cycleCount}
     * of {@code Animation.INDEFINITE} will have a {@code totalDuration} of
     * {@code Duration.INDEFINITE}.
     *
     * <p>
     * This is set to cycleDuration * cycleCount.
     */
    private ReadOnlyObjectProperty<Duration> totalDuration;
    private static final Duration DEFAULT_TOTAL_DURATION = Duration.ZERO;

    public final Duration getTotalDuration() {
        return (totalDuration == null)? DEFAULT_TOTAL_DURATION : totalDuration.get();
    }

    public final ReadOnlyObjectProperty<Duration> totalDurationProperty() {
        if (totalDuration == null)
            totalDuration = new AnimationReadOnlyProperty<>("totalDuration", DEFAULT_TOTAL_DURATION);
        return totalDuration;
    }

    private void updateTotalDuration() {
        // Implementing the bind eagerly, because cycleCount and
        // cycleDuration should not change that often
        int cycleCount = getCycleCount();
        Duration cycleDuration = getCycleDuration();
        Duration newTotalDuration = Duration.ZERO.equals(cycleDuration) ? Duration.ZERO
                : (cycleCount == Animation.INDEFINITE) ? INDEFINITE_DURATION
                : (cycleCount <= 1) ? cycleDuration : Duration.millis(cycleDuration.toMillis() * cycleCount);
        if ((totalDuration != null) || (!DEFAULT_TOTAL_DURATION.equals(newTotalDuration)))
            ((AnimationReadOnlyProperty<Duration>)totalDurationProperty()).set(newTotalDuration);
        if (getStatus() == Status.STOPPED) {
            syncClipEnvelope();
            if (newTotalDuration.compareTo(getCurrentTime()) < 0)
                clipEnvelope.jumpTo(fromDuration(newTotalDuration));
        }
    }

    /**
     * Defines the {@code Animation}'s play head position.
     */
    private CurrentTimeProperty currentTime;
    private long currentTicks;
    private class CurrentTimeProperty extends SimpleObjectProperty<Duration> {

        @Override
        public Object getBean() {
            return Animation.this;
        }

        @Override
        public String getName() {
            return "currentTime";
        }

        @Override
        public Duration get() {
            return getCurrentTime();
        }

        @Override
        public void fireValueChangedEvent() {
            super.fireValueChangedEvent();
        }

    }

    public final Duration getCurrentTime() {
        return TickCalculation.toDuration(currentTicks);
    }

    public final ReadOnlyObjectProperty<Duration> currentTimeProperty() {
        if (currentTime == null) {
            currentTime = new CurrentTimeProperty();
        }
        return currentTime;
    }

    /**
     * Delays the start of an animation.
     *
     * Cannot be negative. Setting to a negative number will result in {@link IllegalArgumentException}.
     */
    private ObjectProperty<Duration> delay;
    private static final Duration DEFAULT_DELAY = Duration.ZERO;

    public final void setDelay(Duration value) {
        if ((delay != null) || (!DEFAULT_DELAY.equals(value)))
            delayProperty().set(value);
    }

    public final Duration getDelay() {
        return (delay == null)? DEFAULT_DELAY : delay.get();
    }

    public final ObjectProperty<Duration> delayProperty() {
        if (delay == null)
            delay = new ObjectPropertyBase<Duration>(DEFAULT_DELAY) {

                @Override
                public Object getBean() {
                    return Animation.this;
                }

                @Override
                public String getName() {
                    return "delay";
                }

                @Override
                protected void invalidated() {
                    final Duration newDuration = get();
                    if (newDuration.lessThan(Duration.ZERO)) {
                        if (isBound())
                            unbind();
                        set(Duration.ZERO);
                        throw new IllegalArgumentException("Cannot set delay to negative value. Setting to Duration.ZERO");
                    }
                }
            };
        return delay;
    }

    /**
     * Defines the number of cycles in this animation. The {@code cycleCount}
     * may be {@code INDEFINITE} for animations that repeat indefinitely, but
     * must otherwise be > 0.
     * <p>
     * It is not possible to change the {@code cycleCount} of a running
     * {@code Animation}. If the value of {@code cycleCount} is changed for a
     * running {@code Animation}, the animation has to be stopped and started again to pick
     * up the new value.
     *
     */
    private Property<Integer> cycleCount;
    private static final int DEFAULT_CYCLE_COUNT = 1;

    public final void setCycleCount(int value) {
        if ((cycleCount != null) || (value != DEFAULT_CYCLE_COUNT))
            cycleCountProperty().setValue(value);
    }

    public final int getCycleCount() {
        return (cycleCount == null)? DEFAULT_CYCLE_COUNT : cycleCount.getValue();
    }

    public final Property<Integer> cycleCountProperty() {
        if (cycleCount == null)
            cycleCount = new SimpleObjectProperty<>(Animation.this, "cycleCount", DEFAULT_CYCLE_COUNT);
        return cycleCount;
    }

    /**
     * Defines whether this
     * {@code Animation} reverses direction on alternating cycles. If
     * {@code true}, the
     * {@code Animation} will proceed forward on the first cycle,
     * then reverses on the second cycle, and so on. Otherwise, animation will
     * loop such that each cycle proceeds forward from the start.
     *
     * It is not possible to change the {@code autoReverse} flag of a running
     * {@code Animation}. If the value of {@code autoReverse} is changed for a
     * running {@code Animation}, the animation has to be stopped and started again to pick
     * up the new value.
     */
    private BooleanProperty autoReverse;
    private static final boolean DEFAULT_AUTO_REVERSE = false;

    public final void setAutoReverse(boolean value) {
        if ((autoReverse != null) || (value != DEFAULT_AUTO_REVERSE))
            autoReverseProperty().setValue(value);
    }

    public final boolean isAutoReverse() {
        return (autoReverse == null)? DEFAULT_AUTO_REVERSE : autoReverse.getValue();
    }

    public final BooleanProperty autoReverseProperty() {
        if (autoReverse == null)
            autoReverse = new SimpleBooleanProperty(this, "autoReverse", DEFAULT_AUTO_REVERSE);
        return autoReverse;
    }

    /**
     * The status of the {@code Animation}.
     *
     * In {@code Animation} can be in one of three states:
     * {@link Status#STOPPED}, {@link Status#PAUSED} or {@link Status#RUNNING}.
     */
    private ReadOnlyObjectProperty<Status> status;
    private static final Status DEFAULT_STATUS = Status.STOPPED;

    protected final void setStatus(Status value) {
        if ((status != null) || (!DEFAULT_STATUS.equals(value)))
            ((AnimationReadOnlyProperty<Status>)statusProperty()).set(value);
    }

    public final Status getStatus() {
        return (status == null)? DEFAULT_STATUS : status.get();
    }

    public final ReadOnlyObjectProperty<Status> statusProperty() {
        if (status == null)
            status = new AnimationReadOnlyProperty<>("status", Status.STOPPED);
        return status;
    }

    private final double targetFramerate;
    private final int resolution;
    private long lastPulse;

    /**
     * The target framerate is the maximum framerate at which this {@code Animation}
     * will run, in frames per second. This can be used, for example, to keep
     * particularly complex {@code Animations} from over-consuming system resources.
     * By default, an {@code Animation}'s framerate is not explicitly limited, meaning
     * the {@code Animation} will run at an optimal framerate for the underlying platform.
     *
     * @return the target framerate
     */
    public final double getTargetFramerate() {
        return targetFramerate;
    }

    /**
     * The action to be executed at the conclusion of this {@code Animation}.
     */
    private ObjectProperty<EventHandler<ActionEvent>> onFinished;
    private static final EventHandler<ActionEvent> DEFAULT_ON_FINISHED = null;

    public final void setOnFinished(EventHandler<ActionEvent> value) {
        if ((onFinished != null) || (value != null /* DEFAULT_ON_FINISHED */))
            onFinishedProperty().set(value);
    }

    public final EventHandler<ActionEvent> getOnFinished() {
        return (onFinished == null)? DEFAULT_ON_FINISHED : onFinished.get();
    }

    public final ObjectProperty<EventHandler<ActionEvent>> onFinishedProperty() {
        if (onFinished == null)
            onFinished = new SimpleObjectProperty<>(this, "onFinished", DEFAULT_ON_FINISHED);
        return onFinished;
    }

    private final ObservableMap<String, Duration> cuePoints = FXCollections
            .observableMap(new HashMap<String, Duration>(0));

    /**
     * The cue points can be
     * used to mark important positions of the {@code Animation}. Once a cue
     * point was defined, it can be used as an argument of
     * {@link #jumpTo(String) jumpTo()} and {@link #playFrom(String) playFrom()}
     * to move to the associated position quickly.
     * <p>
     * Every {@code Animation} has two predefined cue points {@code "start"} and
     * {@code "end"}, which are set at the start respectively the end of the
     * {@code Animation}. The predefined cuepoints do not appear in the map,
     * attempts to override them have no effect.
     * <p>
     * Another option to define a cue point in a {@code Animation} is to set the
     * {@link KeyFrame#name} property of a {@link KeyFrame}.
     *
     * @return {@link javafx.collections.ObservableMap} of cue points
     */
    public final ObservableMap<String, Duration> getCuePoints() {
        return cuePoints;
    }

    /**
     * Jumps to a given position in this {@code Animation}.
     *
     * If the given time is less than {@link Duration#ZERO}, this method will
     * jump to the start of the animation. If the given time is larger than the
     * duration of this {@code Animation}, this method will jump to the end.
     *
     * @param time
     *            the new position
     * @throws NullPointerException
     *             if {@code time} is {@code null}
     * @throws IllegalStateException
     *             if embedded in another animation,
     *                such as {@link SequentialTransition} or {@link ParallelTransition}
     */
    public void jumpTo(Duration time) {
        if (time == null)
            throw new NullPointerException("Time needs to be specified.");
/*
        if (time.isUnknown()) {
            throw new IllegalArgumentException("The time is invalid");
        }
*/
        if (parent != null)
            throw new IllegalStateException("Cannot jump when embedded in another animation");

        lastPlayedFinished = false;

        Duration totalDuration = getTotalDuration();
        time = time.lessThan(Duration.ZERO) ? Duration.ZERO : time.compareTo(totalDuration) > 0 ? totalDuration : time;
        long ticks = fromDuration(time);

        if (getStatus() == Status.STOPPED)
            syncClipEnvelope();
        clipEnvelope.jumpTo(ticks);
    }

    /**
     * Jumps to a predefined position in this {@code Animation}. This method
     * looks for an entry in cue points and jumps to the associated
     * position, if it finds one.
     * <p>
     * If the cue point is behind the end of this {@code Animation}, calling
     * {@code jumpTo} will result in a jump to the end. If the cue point has a
     * negative {@link javafx.util.Duration} it will result in a jump to the
     * beginning. If the cue point has a value of
     * {@link javafx.util.Duration#UNKNOWN} calling {@code jumpTo} will have no
     * effect for this cue point.
     * <p>
     * There are two predefined cue points {@code "start"} and {@code "end"}
     * which are defined to be at the start respectively the end of this
     * {@code Animation}.
     *
     * @param cuePoint
     *            the name of the cue point
     * @throws NullPointerException
     *             if {@code cuePoint} is {@code null}
     * @throws IllegalStateException
     *             if embedded in another animation,
     *                such as {@link SequentialTransition} or {@link ParallelTransition}
     * @see #getCuePoints()
     */
    public void jumpTo(String cuePoint) {
        if (cuePoint == null)
            throw new NullPointerException("CuePoint needs to be specified");
        if ("start".equalsIgnoreCase(cuePoint))
            jumpTo(Duration.ZERO);
        else if ("end".equalsIgnoreCase(cuePoint))
            jumpTo(getTotalDuration());
        else {
            Duration target = getCuePoints().get(cuePoint);
            if (target != null)
                jumpTo(target);
        }
    }

    /**
     * A convenience method to play this {@code Animation} from a predefined
     * position. The position has to be predefined in cue points.
     * Calling this method is equivalent to
     *
     * <pre>
     * <code>
     * animation.jumpTo(cuePoint);
     * animation.play();
     * </code>
     * </pre>
     *
     * Note that unlike {@link #playFromStart()} calling this method will not
     * change the playing direction of this {@code Animation}.
     *
     * @param cuePoint
     *            name of the cue point
     * @throws NullPointerException
     *             if {@code cuePoint} is {@code null}
     * @throws IllegalStateException
     *             if embedded in another animation,
     *                such as {@link SequentialTransition} or {@link ParallelTransition}
     * @see #getCuePoints()
     */
    public void playFrom(String cuePoint) {
        jumpTo(cuePoint);
        play();
    }

    /**
     * A convenience method to play this {@code Animation} from a specific
     * position. Calling this method is equivalent to
     *
     * <pre>
     * <code>
     * animation.jumpTo(time);
     * animation.play();
     * </code>
     * </pre>
     *
     * Note that unlike {@link #playFromStart()} calling this method will not
     * change the playing direction of this {@code Animation}.
     *
     * @param time
     *            position where to play from
     * @throws NullPointerException
     *             if {@code time} is {@code null}
     * @throws IllegalStateException
     *             if embedded in another animation,
     *                such as {@link SequentialTransition} or {@link ParallelTransition}
     */
    public void playFrom(Duration time) {
        jumpTo(time);
        play();
    }

    /**
     * Plays {@code Animation} from current position in the direction indicated
     * by {@code rate}. If the {@code Animation} is running, it has no effect.
     * <p>
     * When {@code rate} > 0 (forward play), if an {@code Animation} is already
     * positioned at the end, the first cycle will not be played, it is
     * considered to have already finished. This also applies to a backward (
     * {@code rate} < 0) cycle if an {@code Animation} is positioned at the beginning.
     * However, if the {@code Animation} has {@code cycleCount} > 1, following
     * cycle(s) will be played as usual.
     * <p>
     * When the {@code Animation} reaches the end, the {@code Animation} is stopped and
     * the play head remains at the end.
     * <p>
     * To play an {@code Animation} backwards from the end:<br>
     * <code>
     *  animation.setRate(negative rate);<br>
     *  animation.jumpTo(overall duration of animation);<br>
     *  animation.play();<br>
     * </code>
     * <p>
     * Note: <ul>
     * <li>{@code play()} is an asynchronous call, the {@code Animation} may not
     * start immediately. </ul>
     *
     * @throws IllegalStateException
     *             if embedded in another animation,
     *                such as {@link SequentialTransition} or {@link ParallelTransition}
     */
    public void play() {
        if (parent != null)
            throw new IllegalStateException("Cannot start when embedded in another animation");
        switch (getStatus()) {
            case STOPPED:
                if (impl_startable(true)) {
                    double rate = getRate();
                    if (lastPlayedFinished)
                        jumpTo((rate < 0)? getTotalDuration() : Duration.ZERO);
                    impl_start(true);
                    startReceiver(fromDuration(getDelay()));
                    if (Math.abs(rate) < EPSILON)
                        pauseReceiver();
                    else {
                    }
                } else
                    callOnFinishedHandler();
                break;
            case PAUSED:
                impl_resume();
                if (Math.abs(getRate()) >= EPSILON)
                    resumeReceiver();
                break;
        }
    }

    protected void callOnFinishedHandler() {
        EventHandler<ActionEvent> handler = getOnFinished();
        if (handler != null)
            handler.handle(null /*new ActionEvent(this, null)*/);
    }

    /**
     * Plays an {@code Animation} from initial position in forward direction.
     * <p>
     * It is equivalent to
     * <p>
     * <code>
     *      animation.stop();<br>
     *      animation.setRate = setRate(Math.abs(animation.getRate())); </br>
     *      animation.jumpTo(Duration.ZERO);<br>
     *      animation.play();<br>
     *  </code>
     *
     * <p>
     * Note: <ul>
     * <li>{@code playFromStart()} is an asynchronous call, {@code Animation} may
     * not start immediately. </ul>
     * <p>
     *
     * @throws IllegalStateException
     *             if embedded in another animation,
     *                such as {@link SequentialTransition} or {@link ParallelTransition}
     */
    public void playFromStart() {
        stop();
        setRate(Math.abs(getRate()));
        jumpTo(Duration.ZERO);
        play();
    }

    /**
     * Stops the animation and resets the play head to its initial position. If
     * the animation is not currently running, this method has no effect.
     * <p>
     * Note: <ul>
     * <li>{@code stop()} is an asynchronous call, the {@code Animation} may not stop
     * immediately. </ul>
     * @throws IllegalStateException
     *             if embedded in another animation,
     *                such as {@link SequentialTransition} or {@link ParallelTransition}
     */
    public void stop() {
        if (parent != null)
            throw new IllegalStateException("Cannot stop when embedded in another animation");
        if (getStatus() != Status.STOPPED) {
            clipEnvelope.abortCurrentPulse();
            impl_stop();
            jumpTo(Duration.ZERO);
        }
    }

    /**
     * Pauses the animation. If the animation is not currently running, this
     * method has no effect.
     * <p>
     * Note: <ul>
     * <li>{@code pause()} is an asynchronous call, the {@code Animation} may not pause
     * immediately. </ul>
     * @throws IllegalStateException
     *             if embedded in another animation,
     *                such as {@link SequentialTransition} or {@link ParallelTransition}
     */
    public void pause() {
        if (parent != null)
            throw new IllegalStateException("Cannot pause when embedded in another animation");
        if (getStatus() == Status.RUNNING) {
            clipEnvelope.abortCurrentPulse();
            pauseReceiver();
            impl_pause();
        }
    }

    /**
     * The constructor of {@code Animation}.
     *
     * This constructor allows to define a target framerate.
     *
     * @param targetFramerate
     *            The custom target frame rate for this {@code Animation}
     * @see #getTargetFramerate()
     */
    protected Animation(double targetFramerate) {
        this.targetFramerate = targetFramerate;
        this.resolution = (int) Math.max(1, Math.round(TickCalculation.TICKS_PER_SECOND / targetFramerate));
        this.clipEnvelope = ClipEnvelope.create(this);
        this.timer = MasterTimer.get();
    }

    /**
     * The constructor of {@code Animation}.
     */
    protected Animation() {
        this.resolution = 1;
        this.targetFramerate = TickCalculation.TICKS_PER_SECOND / MasterTimer.get().getDefaultResolution();
        this.clipEnvelope = ClipEnvelope.create(this);
        this.timer = MasterTimer.get();
    }

    // These constructors are only for testing purposes
    Animation(AbstractMasterTimer timer) {
        this.resolution = 1;
        this.targetFramerate = TickCalculation.TICKS_PER_SECOND / timer.getDefaultResolution();
        this.clipEnvelope = ClipEnvelope.create(this);
        this.timer = timer;
    }

    // These constructors are only for testing purposes
    Animation(AbstractMasterTimer timer, ClipEnvelope clipEnvelope, int resolution) {
        this.resolution = resolution;
        this.targetFramerate = TickCalculation.TICKS_PER_SECOND / resolution;
        this.clipEnvelope = clipEnvelope;
        this.timer = timer;
    }

    boolean impl_startable(boolean forceSync) {
        return (fromDuration(getCycleDuration()) > 0L)
                || (!forceSync && clipEnvelope.wasSynched());
    }

    void impl_sync(boolean forceSync) {
        if (forceSync || !clipEnvelope.wasSynched())
            syncClipEnvelope();
    }

    private void syncClipEnvelope() {
        int publicCycleCount = getCycleCount();
        int internalCycleCount = (publicCycleCount <= 0)
                && (publicCycleCount != INDEFINITE) ? 1 : publicCycleCount;
        clipEnvelope = clipEnvelope.setCycleCount(internalCycleCount);
        clipEnvelope.setCycleDuration(getCycleDuration());
        clipEnvelope.setAutoReverse(isAutoReverse());
    }

    void impl_start(boolean forceSync) {
        impl_sync(forceSync);
        setStatus(Status.RUNNING);
        clipEnvelope.start();
        setCurrentRate(clipEnvelope.getCurrentRate());
        lastPulse = 0;
    }

    void impl_pause() {
        final double currentRate = getCurrentRate();
        if (Math.abs(currentRate) >= EPSILON)
            lastPlayedForward = Math.abs(getCurrentRate() - getRate()) < EPSILON;
        setCurrentRate(0.0);
        setStatus(Status.PAUSED);
    }

    void impl_resume() {
        setStatus(Status.RUNNING);
        setCurrentRate(lastPlayedForward ? getRate() : -getRate());
    }

    void impl_stop() {
        if (!paused)
            timer.removePulseReceiver(pulseReceiver);
        setStatus(Status.STOPPED);
        setCurrentRate(0.0);
    }

    void impl_timePulse(long elapsedTime) {
        if (resolution == 1) // fullspeed
            clipEnvelope.timePulse(elapsedTime);
        else if (elapsedTime - lastPulse >= resolution) {
            lastPulse = (elapsedTime / resolution) * resolution;
            clipEnvelope.timePulse(elapsedTime);
        }
    }

    abstract void impl_playTo(long currentTicks, long cycleTicks);

    abstract void impl_jumpTo(long currentTicks, long cycleTicks, boolean forceJump);

    void impl_setCurrentTicks(long ticks) {
        currentTicks = ticks;
        if (currentTime != null)
            currentTime.fireValueChangedEvent();
    }

    void impl_setCurrentRate(double currentRate) {
//        if (getStatus() == Status.RUNNING) {
        setCurrentRate(currentRate);
//        }
    }

    final void impl_finished() {
        lastPlayedFinished = true;
        impl_stop();
        final EventHandler<ActionEvent> handler = getOnFinished();
        if (handler != null) {
            try {
                handler.handle(null /*new ActionEvent(this, null)*/);
            } catch (Exception ex) {
                //Thread.currentThread().getUncaughtExceptionHandler().uncaughtException(Thread.currentThread(), ex);
                ex.printStackTrace();
            }
        }
    }
}
