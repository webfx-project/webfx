package mongoose.backend.activities.loadtester.drive.metrics.model;

/**
 * @author Jean-Pierre Alonso.
 */
public class MemData {
    private long committedMem;
    private long totalMem;
    private long usedMem;
    private long freeMem;
    private long maxMem;
    private long freePhMem;
    private long totalPhMem;

    public MemData() {
        super();
    }

    public MemData(SysBean sb) {
        committedMem = sb.getCommittedMem();
        totalMem= sb.getTotalMem();
        usedMem = sb.getUsedMem();
        freeMem = sb.getFreeMem();
        maxMem = sb.getMaxMem();
        freePhMem = sb.getFreePhMem();
        totalPhMem = sb.getTotalPhMem();
    }

    public void set (MemData memData) {
        committedMem = memData.committedMem;
        totalMem = memData.totalMem;
        usedMem = memData.usedMem;
        freeMem = memData.freeMem;
        maxMem = memData.maxMem;
        freePhMem = memData.freePhMem;
        totalPhMem = memData.totalPhMem;
    }

    public long committedMem() {
        return committedMem;
    }

    public long totalMem() {
        return totalMem;
    }

    public long usedMem() {
        return usedMem;
    }

    public long freeMem() {
        return freeMem;
    }

    public long maxMem() {
        return maxMem;
    }

    public long freePhMem() {
        return freePhMem;
    }

    public long totalPhMem() {
        return totalPhMem;
    }
}
