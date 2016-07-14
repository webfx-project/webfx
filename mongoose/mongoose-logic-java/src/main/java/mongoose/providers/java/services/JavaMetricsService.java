package mongoose.providers.java.services;

import mongoose.services.Metrics;
import mongoose.services.MetricsService;

/**
 * @author Bruno Salmon
 */
public class JavaMetricsService implements MetricsService {

    @Override
    public void takeMetricsSnapshot(Metrics metrics) {
        Runtime runtime = Runtime.getRuntime();
        metrics.setMemoryFree(runtime.freeMemory());
        metrics.setMemoryMax(runtime.maxMemory());
        metrics.setMemoryTotal(runtime.totalMemory());
    }
}
