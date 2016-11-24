package naga.toolkit.drawing.animation;

/**
 * @author Bruno Salmon
 */
public interface DelayedRunnable extends Runnable {
    /**
     * Gets the delay <strong>in milliseconds</strong>.
     * @return delay in millis
     */
    long getDelay();
}