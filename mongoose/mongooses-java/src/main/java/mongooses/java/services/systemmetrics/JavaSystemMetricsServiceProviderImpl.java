package mongooses.java.services.systemmetrics;

import com.sun.management.OperatingSystemMXBean;
import mongooses.core.services.systemmetrics.SystemMetrics;
import mongooses.core.services.systemmetrics.spi.SystemMetricsServiceProvider;

import java.lang.management.ManagementFactory;

/**
 * @author Bruno Salmon
 */
public class JavaSystemMetricsServiceProviderImpl implements SystemMetricsServiceProvider {

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
