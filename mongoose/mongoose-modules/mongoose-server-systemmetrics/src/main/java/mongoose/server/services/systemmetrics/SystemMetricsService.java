package mongoose.server.services.systemmetrics;

import mongoose.server.services.systemmetrics.spi.SystemMetricsServiceProvider;
import mongoose.shared.services.systemmetrics.SystemMetrics;
import webfx.platforms.core.util.serviceloader.SingleServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class SystemMetricsService {

    public static SystemMetricsServiceProvider getProvider() {
        return SingleServiceLoader.loadService(SystemMetricsServiceProvider.class, SingleServiceLoader.NotFoundPolicy.TRACE_AND_RETURN_NULL);
    }

    public static void takeSystemMetricsSnapshot(SystemMetrics systemMetrics) {
        getProvider().takeSystemMetricsSnapshot(systemMetrics);
    }

}
