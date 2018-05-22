package mongoose.providers.java.metrics;

import com.sun.management.OperatingSystemMXBean;
import mongoose.services.metrics.Metrics;
import mongoose.services.metrics.spi.MetricsServiceProvider;

import java.lang.management.ManagementFactory;

/**
 * @author Bruno Salmon
 */
public class JavaMetricsServiceProvider implements MetricsServiceProvider {

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
