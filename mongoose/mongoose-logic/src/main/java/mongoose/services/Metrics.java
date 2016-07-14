package mongoose.services;

import java.time.Instant;

/**
 * @author Bruno Salmon
 */
public interface Metrics {

    void setDate(Instant date);

    Instant getDate();

    void setMemoryTotal(Long memoryTotal);

    Long getMemoryTotal();

    void setMemoryFree(Long memoryFree);

    Long getMemoryFree();

    void setMemoryMax(Long memoryMax);

    Long getMemoryMax();

    void setSystemLoadAverage(Long systemLoadAverage);

    Long getSystemLoadAverage();

    void setProcessCpuLoad(Long processCpuLoad);

    Long getProcessCpuLoad();

    Long getMemoryUsed();
}
