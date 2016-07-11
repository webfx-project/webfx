package mongoose.activities.monitor.metrics.model;

import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.Property;

/**
 * @author Jean-Pierre Alonso.
 */
public class SysBeanFX {
    private final Property<String> committedMem   = new SimpleObjectProperty();
    private final Property<String> totalMem       = new SimpleObjectProperty();
    private final Property<String> usedMem        = new SimpleObjectProperty();
    private final Property<String> freeMem        = new SimpleObjectProperty();
    private final Property<String> maxMem         = new SimpleObjectProperty();
    private final Property<String> freePhMem      = new SimpleObjectProperty();
    private final Property<String> totalPhMem     = new SimpleObjectProperty();
    private final Property<String> sysLoad        = new SimpleObjectProperty();   // the "recent cpu usage" for the whole system, during the last minute (-1 if not available).
    private final Property<String> cpuLoad        = new SimpleObjectProperty();   // the "recent cpu usage" for the Java Virtual Machine process.
    private final Property<String> cpuTime        = new SimpleObjectProperty();   // the CPU time used by the process on which the Java virtual machine is running in nanoseconds.
    private final Property<String> processors     = new SimpleObjectProperty();

    public SysBeanFX () {
        super();
    }

    public SysBeanFX (SysBean sb) {
        committedMem.setValue(Long.toString(sb.getCommittedMem()/SysBean.MB));
        totalMem.setValue(Long.toString(sb.getTotalMem()/SysBean.MB));
        usedMem.setValue(Long.toString(sb.getUsedMem()/SysBean.MB));
        freeMem.setValue(Long.toString(sb.getFreeMem()/SysBean.MB));
        maxMem.setValue(Long.toString(sb.getMaxMem()/SysBean.MB));
        freePhMem.setValue(Long.toString(sb.getFreePhMem()/SysBean.MB));
        totalPhMem.setValue(Long.toString(sb.getTotalPhMem()/SysBean.MB));
        sysLoad.setValue(Double.toString(sb.getSysLoad()==-1?-1:sb.getSysLoad()*100));
        cpuLoad.setValue(Double.toString(sb.getCpuLoad()*100));
        cpuTime.setValue(Long.toString(sb.getCpuTime()/SysBean.NANO));
        processors.setValue(Integer.toString(sb.getAvailableProcessors()));
    }

    public void set (SysBeanFX sb) {
        committedMem.setValue(sb.committedMem.getValue());
        totalMem.setValue(sb.totalMem.getValue());
        usedMem.setValue(sb.usedMem.getValue());
        freeMem.setValue(sb.freeMem.getValue());
        maxMem.setValue(sb.maxMem.getValue());
        freePhMem.setValue(sb.freePhMem.getValue());
        totalPhMem.setValue(sb.totalPhMem.getValue());
        sysLoad.setValue(sb.sysLoad.getValue());
        cpuLoad.setValue(sb.cpuLoad.getValue());
        cpuTime.setValue(sb.cpuTime.getValue());
        processors.setValue(sb.processors.getValue());
    }

    public Property<String> committedMemProperty() {
        return committedMem;
    }

    public Property<String> totalMemProperty() {
        return totalMem;
    }

    public Property<String> usedMemProperty() {
        return usedMem;
    }

    public Property<String> freeMemProperty() {
        return freeMem;
    }

    public Property<String> maxMemProperty() {
        return maxMem;
    }

    public Property<String> freePhMemProperty() {
        return freePhMem;
    }

    public Property<String> totalPhMemProperty() {
        return totalPhMem;
    }

    public Property<String> sysLoadProperty() {
        return sysLoad;
    }

    public Property<String> cpuLoadProperty() {
        return cpuLoad;
    }

    public Property<String> cpuTimeProperty() {
        return cpuTime;
    }

    public Property<String> processorsProperty() {
        return processors;
    }

}
