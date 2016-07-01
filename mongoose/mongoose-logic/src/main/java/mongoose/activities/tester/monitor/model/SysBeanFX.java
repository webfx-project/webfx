package mongoose.activities.tester.monitor.model;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * @author Jean-Pierre Alonso.
 */
public class SysBeanFX {
    private final StringProperty committedMem   = new SimpleStringProperty();
    private final StringProperty totalMem       = new SimpleStringProperty();
    private final StringProperty usedMem        = new SimpleStringProperty();
    private final StringProperty freeMem        = new SimpleStringProperty();
    private final StringProperty maxMem         = new SimpleStringProperty();
    private final StringProperty freePhMem      = new SimpleStringProperty();
    private final StringProperty totalPhMem     = new SimpleStringProperty();
    private final StringProperty sysLoad        = new SimpleStringProperty();   // the "recent cpu usage" for the whole system, during the last minute (-1 if not available).
    private final StringProperty cpuLoad        = new SimpleStringProperty();   // the "recent cpu usage" for the Java Virtual Machine process.
    private final StringProperty cpuTime        = new SimpleStringProperty();   // the CPU time used by the process on which the Java virtual machine is running in nanoseconds.
    private final StringProperty processors     = new SimpleStringProperty();

    public SysBeanFX () {
        super();
    }

    public SysBeanFX (SysBean sb) {
        committedMem.set(Long.toString(sb.getCommittedMem()/SysBean.MB));
        totalMem.set(Long.toString(sb.getTotalMem()/SysBean.MB));
        usedMem.set(Long.toString(sb.getUsedMem()/SysBean.MB));
        freeMem.set(Long.toString(sb.getFreeMem()/SysBean.MB));
        maxMem.set(Long.toString(sb.getMaxMem()/SysBean.MB));
        freePhMem.set(Long.toString(sb.getFreePhMem()/SysBean.MB));
        totalPhMem.set(Long.toString(sb.getTotalPhMem()/SysBean.MB));
        sysLoad.set(Double.toString(sb.getSysLoad()==-1?-1:sb.getSysLoad()*100));
        cpuLoad.set(Double.toString(sb.getCpuLoad()*100));
        cpuTime.set(Long.toString(sb.getCpuTime()/SysBean.NANO));
        processors.set(Integer.toString(sb.getAvailableProcessors()));
    }

    public void set (SysBeanFX sb) {
        committedMem.set(sb.committedMem.get());
        totalMem.set(sb.totalMem.get());
        usedMem.set(sb.usedMem.get());
        freeMem.set(sb.freeMem.get());
        maxMem.set(sb.maxMem.get());
        freePhMem.set(sb.freePhMem.get());
        totalPhMem.set(sb.totalPhMem.get());
        sysLoad.set(sb.sysLoad.get());
        cpuLoad.set(sb.cpuLoad.get());
        cpuTime.set(sb.cpuTime.get());
        processors.set(sb.processors.get());
    }

    public StringProperty committedMemProperty() {
        return committedMem;
    }

    public StringProperty totalMemProperty() {
        return totalMem;
    }

    public StringProperty usedMemProperty() {
        return usedMem;
    }

    public StringProperty freeMemProperty() {
        return freeMem;
    }

    public StringProperty maxMemProperty() {
        return maxMem;
    }

    public StringProperty freePhMemProperty() {
        return freePhMem;
    }

    public StringProperty totalPhMemProperty() {
        return totalPhMem;
    }

    public StringProperty sysLoadProperty() {
        return sysLoad;
    }

    public StringProperty cpuLoadProperty() {
        return cpuLoad;
    }

    public StringProperty cpuTimeProperty() {
        return cpuTime;
    }

    public StringProperty processorsProperty() {
        return processors;
    }

}
