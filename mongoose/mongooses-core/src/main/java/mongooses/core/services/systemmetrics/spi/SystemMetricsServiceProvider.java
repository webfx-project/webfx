package mongooses.core.services.systemmetrics.spi;

import mongooses.core.services.systemmetrics.SystemMetrics;

/**
 * @author Bruno Salmon
 */
public interface SystemMetricsServiceProvider {

    void takeSystemMetricsSnapshot(SystemMetrics systemMetrics);

}
