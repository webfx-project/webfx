package mongoose.services.systemmetrics;

import mongoose.services.systemmetrics.spi.SystemMetricsServiceProvider;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class SystemMetricsService {

    public static SystemMetricsServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(SystemMetricsServiceProvider.class, ServiceLoaderHelper.NotFoundPolicy.TRACE_AND_RETURN_NULL);
    }

    public static void takeSystemMetricsSnapshot(SystemMetrics systemMetrics) {
        getProvider().takeSystemMetricsSnapshot(systemMetrics);
    }

}
