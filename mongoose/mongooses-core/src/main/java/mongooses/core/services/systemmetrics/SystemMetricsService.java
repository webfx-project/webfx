package mongooses.core.services.systemmetrics;

import mongooses.core.services.systemmetrics.spi.SystemMetricsServiceProvider;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public class SystemMetricsService {

    public static SystemMetricsServiceProvider getProvider() {
        return SingleServiceLoader.loadService(SystemMetricsServiceProvider.class, SingleServiceLoader.NotFoundPolicy.TRACE_AND_RETURN_NULL);
    }

    public static void takeSystemMetricsSnapshot(SystemMetrics systemMetrics) {
        getProvider().takeSystemMetricsSnapshot(systemMetrics);
    }

}
