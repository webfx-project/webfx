package mongoose.client.util.log;

import webfx.platform.shared.services.log.Logger;

/**
 * @author Bruno Salmon
 */
public final class PerformanceLogger {

    private long t0 = System.currentTimeMillis();

    public void log(String message) {
        long t1 = System.currentTimeMillis();
        Logger.log(message + ": " + (t1 - t0) + "ms");
        t0 = t1;
    }
}
