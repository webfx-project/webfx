package dev.webfx.platform.shared.services.scheduler;

/**
 * @author Bruno Salmon
 */
public interface Scheduled {
    /**
     * Cancel the scheduled task. Returns {@code} true if the task was successfully cancelled.
     */
    boolean cancel();
}
