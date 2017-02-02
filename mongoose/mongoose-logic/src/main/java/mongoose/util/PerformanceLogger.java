package mongoose.util;

import naga.platform.spi.Platform;

/**
 * @author Bruno Salmon
 */
public class PerformanceLogger {

    private long t0 = System.currentTimeMillis();

    public void log(String message) {
        long t1 = System.currentTimeMillis();
        Platform.log(message + ": " + (t1 - t0) + "ms");
        t0 = t1;
    }
}
