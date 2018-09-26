package mongooses.java.server.services.systemmetrics;

import com.sun.management.OperatingSystemMXBean;
import mongooses.core.server.services.systemmetrics.SystemMetrics;
import mongooses.core.server.services.systemmetrics.spi.SystemMetricsServiceProvider;

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
