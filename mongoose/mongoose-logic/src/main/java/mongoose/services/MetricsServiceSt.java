package mongoose.services;

import naga.commons.util.serviceloader.ServiceLoaderHelper;

/**
 * @author Bruno Salmon
 */
class MetricsServiceSt {

    private static MetricsService METRICS_SERVICE;

    static synchronized MetricsService get() {
        if (METRICS_SERVICE == null)
            METRICS_SERVICE = ServiceLoaderHelper.loadService(MetricsService.class, ServiceLoaderHelper.NotFoundPolicy.TRACE_AND_RETURN_NULL);
        return METRICS_SERVICE;
    }
}
