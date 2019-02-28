package mongoose.server.services.systemmetrics;

import mongoose.server.services.systemmetrics.spi.SystemMetricsServiceProvider;
import mongoose.shared.services.systemmetrics.SystemMetrics;
import webfx.platform.shared.util.serviceloader.SingleServiceProvider;

import java.util.ServiceLoader;

/**
 * @author Bruno Salmon
 */
public final class SystemMetricsService {

    public static SystemMetricsServiceProvider getProvider() {
        return SingleServiceProvider.getProvider(SystemMetricsServiceProvider.class, () -> ServiceLoader.load(SystemMetricsServiceProvider.class), SingleServiceProvider.NotFoundPolicy.TRACE_AND_RETURN_NULL);
    }

    public static void takeSystemMetricsSnapshot(SystemMetrics systemMetrics) {
        getProvider().takeSystemMetricsSnapshot(systemMetrics);
    }

}
