package mongoose.providers.java.metrics;

import com.sun.management.OperatingSystemMXBean;
import mongoose.spi.metrics.Metrics;
import mongoose.spi.metrics.MetricsService;

import java.lang.management.ManagementFactory;

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

        OperatingSystemMXBean osMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        metrics.setSystemLoadAverage(osMXBean.getSystemLoadAverage());
        metrics.setProcessCpuLoad(osMXBean.getProcessCpuLoad());

    }
}
