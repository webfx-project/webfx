package mongoose.backend.javafx;

import com.sun.management.OperatingSystemMXBean;
import mongoose.activities.tester.monitor.controller.SystemLookup;
import mongoose.activities.tester.monitor.model.SysBean;

import java.lang.management.ManagementFactory;

/**
 * @author Jean-Pierre Alonso.
 */
public class SystemLookupFX extends SystemLookup {
    private Runtime rt = Runtime.getRuntime();
    private OperatingSystemMXBean osMXBean = ManagementFactory.getPlatformMXBean(OperatingSystemMXBean.class);

    @Override
    public SysBean snapshot() {
        sb = new SysBean();
        sb.setCommittedMem(osMXBean.getCommittedVirtualMemorySize());
        sb.setTotalMem(rt.totalMemory());
        sb.setFreeMem(rt.freeMemory());
        sb.setUsedMem(sb.getTotalMem()-sb.getFreeMem());
        sb.setMaxMem(rt.maxMemory());
        sb.setFreePhMem(osMXBean.getFreePhysicalMemorySize());
        sb.setTotalPhMem(osMXBean.getTotalPhysicalMemorySize());
        sb.setAvailableProcessors(rt.availableProcessors());
        sb.setSysLoad(osMXBean.getSystemLoadAverage());
        sb.setCpuLoad(osMXBean.getProcessCpuLoad());
        sb.setCpuTime(osMXBean.getProcessCpuTime());
        return sb;
    }
}
