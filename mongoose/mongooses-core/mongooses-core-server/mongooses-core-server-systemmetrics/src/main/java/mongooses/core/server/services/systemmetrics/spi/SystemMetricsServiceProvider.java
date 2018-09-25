package mongooses.core.server.services.systemmetrics.spi;

import mongooses.core.server.services.systemmetrics.SystemMetrics;

/**
 * @author Bruno Salmon
 */
public interface SystemMetricsServiceProvider {

    void takeSystemMetricsSnapshot(SystemMetrics systemMetrics);

}
