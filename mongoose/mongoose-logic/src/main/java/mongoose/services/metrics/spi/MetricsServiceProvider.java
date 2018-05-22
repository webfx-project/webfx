package mongoose.services.metrics.spi;

import mongoose.services.metrics.Metrics;

/**
 * @author Bruno Salmon
 */
public interface MetricsServiceProvider {

    void takeMetricsSnapshot(Metrics metrics);

}
