package mongoose.activities.backend.monitor.metrics.model;

import naga.platform.services.log.Logger;

import java.time.Instant;

/**
 * @author Jean-Pierre Alonso.
 */
public class SysBean {
    private Instant date;
    private long    committedMem;
    private long    totalMem;
    private long    usedMem;
    private long    freeMem;
    private long    maxMem;
    private long    freePhMem;
    private long    totalPhMem;
    private double  sysLoad;         // the "recent cpu usage" for the whole system, during the last minute (-1 if not available).
    private double  cpuLoad;         // the "recent cpu usage" for the Java Virtual Machine process.
    private long    cpuTime;           // the CPU time used by the process on which the Java virtual machine is running in nanoseconds.
    private int     availableProcessors;

    static long MB = 1024*1024;
    static long NANO = 1000000;

    public void printState() {

        Logger.log("date     : " + getDate());
        Logger.log("commited : " + getCommittedMem() + " MB");
        Logger.log("total    : " + getTotalMem() + " MB");
        Logger.log("used     : " + getUsedMem() + " MB");
        Logger.log("free     : " + getFreeMem() + " MB\n");
        Logger.log("sys max  : " + getMaxMem() + " MB");
        Logger.log("sys free : " + getFreePhMem() + " MB\n");
        //        Platform.log("proc     : " + getAvailableProcessors());
        Logger.log("sys load : " + getSysLoad());
        Logger.log("cpu load : " + (getCpuLoad()*100) + " %");
        Logger.log("cpu time : " + (getCpuTime()/NANO) + " ms");

    }

    public Instant getDate() {
        return date;
    }

    public void setDate(Instant date) {
        this.date = date;
    }

    public long getCommittedMem() { return committedMem; }

    public void setCommittedMem(long committedMem) { this.committedMem = committedMem;  }

    public long getTotalMem() { return totalMem; }

    public void setTotalMem(long totalMem) {  this.totalMem = totalMem;  }

    public long getUsedMem() { return usedMem;  }

    public void setUsedMem(long usedMem) { this.usedMem = usedMem; }

    public long getFreeMem() { return freeMem; }

    public void setFreeMem(long freeMem) { this.freeMem = freeMem; }

    public long getMaxMem() { return maxMem; }

    public void setMaxMem(long maxMem) { this.maxMem = maxMem; }

    public long getFreePhMem() { return freePhMem; }

    public void setFreePhMem(long freePhMem) { this.freePhMem = freePhMem; }

    public long getTotalPhMem() { return totalPhMem;  }

    public void setTotalPhMem(long totalPhMem) { this.totalPhMem = totalPhMem; }

    public double getSysLoad() { return sysLoad; }

    public void setSysLoad(double sysLoad) { this.sysLoad = sysLoad; }

    public double getCpuLoad() { return cpuLoad; }

    public void setCpuLoad(double cpuLoad) { this.cpuLoad = cpuLoad; }

    public long getCpuTime() { return cpuTime; }

    public void setCpuTime(long cpuTime) { this.cpuTime = cpuTime; }

    public int getAvailableProcessors() { return availableProcessors; }

    public void setAvailableProcessors(int availableProcessors) {
        this.availableProcessors = availableProcessors;
    }

}
