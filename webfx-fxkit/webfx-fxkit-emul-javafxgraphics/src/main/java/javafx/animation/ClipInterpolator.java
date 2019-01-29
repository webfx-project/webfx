package javafx.animation;

import javafx.util.Duration;

/**
 * @author Bruno Salmon
 */
public abstract class ClipInterpolator {

    static ClipInterpolator create(KeyFrame[] keyFrames, long[] keyFrameTicks) {
        return (ClipInterpolator.getRealKeyFrameCount(keyFrames) == 2) ? (keyFrames.length == 1) ? new SimpleClipInterpolator(
                keyFrames[0], keyFrameTicks[0]) : new SimpleClipInterpolator(keyFrames[0],
                keyFrames[1], keyFrameTicks[1])
                : new GeneralClipInterpolator(keyFrames, keyFrameTicks);
    }

    /**
     * Figures out the number of "real" key frames. The user may not have specified the "zero" key
     * frame, in which case we end up inferring an additional zero key frame on the array.
     *
     * @param keyFrames The key frames. Must not be null.
     * @return The "real" number of key frames
     */
    static int getRealKeyFrameCount(KeyFrame[] keyFrames) {
        final int length = keyFrames.length;
        if (length == 0)
            return 0;
        Duration time = keyFrames[0].getTime();
        return !time.lessThanOrEqualTo(Duration.ZERO) ? length + 1 : length;
    }

    /**
     * Changes the keyframes.
     *
     * The optimal implementation for the new keyFrames might be different. For
     * this reason, setKeyFrames returns a ClipInterpolator implementation with
     * the updated values. This can either be the same object or a newly created
     * one.
     *
     * @param keyFrames
     *            The new sorted array of keyframes of this clip
     * @param keyFrameTicks
     *            tick duration of corresponding keyFrames
     * @return The ClipInterpolator implementation to use after changing the
     *         keyframes.
     */
    abstract ClipInterpolator setKeyFrames(KeyFrame[] keyFrames, long[] keyFrameTicks);

    abstract void interpolate(long ticks);

    abstract void validate(boolean forceSync);
}
