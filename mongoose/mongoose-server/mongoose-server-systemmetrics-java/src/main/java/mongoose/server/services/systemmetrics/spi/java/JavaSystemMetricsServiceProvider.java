package mongoose.server.services.systemmetrics.spi.java;

import com.sun.management.OperatingSystemMXBean;
import mongoose.shared.services.systemmetrics.SystemMetrics;
import mongoose.server.services.systemmetrics.spi.SystemMetricsServiceProvider;

import java.lang.management.ManagementFactory;

/**
 * @author Bruno Salmon
 */
public final class JavaSystemMetricsServiceProvider implements SystemMetricsServiceProvider {

    @Override
    public void takeSystemMetricsSnapshot(SystemMetrics systemMetrics) {
        Runtime runtime = Runtime.getRuntime();
        systemMetrics.setMemoryFree(runtime.freeMemory());
        systemMetrics.setMemoryMax(runtime.maxMemory());
        systemMetrics.setMemoryTotal(runtime.totalMemory());

        OperatingSystemMXBean osMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);
        systemMetrics.setSystemLoadAverage(osMXBean.getSystemLoadAverage());
        systemMetrics.setProcessCpuLoad(osMXBean.getProcessCpuLoad());
    }
}
