package mongoose.services.metrics;

import mongoose.services.metrics.spi.MetricsServiceProvider;
import naga.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
public class MetricsService {

    public static synchronized MetricsServiceProvider getProvider() {
        return ServiceLoaderHelper.loadService(MetricsServiceProvider.class, ServiceLoaderHelper.NotFoundPolicy.TRACE_AND_RETURN_NULL);
    }

    public static void takeMetricsSnapshot(Metrics metrics) {
        getProvider().takeMetricsSnapshot(metrics);
    }

}
