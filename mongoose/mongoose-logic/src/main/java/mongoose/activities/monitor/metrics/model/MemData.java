package mongoose.activities.monitor.metrics.model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Jean-Pierre Alonso.
 */
public class MemData {
    private final Property<String> committedMem   = new SimpleObjectProperty();
    private final Property<String> totalMem       = new SimpleObjectProperty();
    private final Property<String> usedMem        = new SimpleObjectProperty();
    private final Property<String> freeMem        = new SimpleObjectProperty();
    private final Property<String> maxMem         = new SimpleObjectProperty();
    private final Property<String> freePhMem      = new SimpleObjectProperty();
    private final Property<String> totalPhMem     = new SimpleObjectProperty();

    public MemData() {
        super();
    }

    public MemData(SysBean sb) {
        committedMem.setValue(Long.toString(sb.getCommittedMem()/SysBean.MB));
        totalMem.setValue(Long.toString(sb.getTotalMem()/SysBean.MB));
        usedMem.setValue(Long.toString(sb.getUsedMem()/SysBean.MB));
        freeMem.setValue(Long.toString(sb.getFreeMem()/SysBean.MB));
        maxMem.setValue(Long.toString(sb.getMaxMem()/SysBean.MB));
        freePhMem.setValue(Long.toString(sb.getFreePhMem()/SysBean.MB));
        totalPhMem.setValue(Long.toString(sb.getTotalPhMem()/SysBean.MB));
    }

    public void set (MemData memData) {
        committedMem.setValue(memData.committedMem.getValue());
        totalMem.setValue(memData.totalMem.getValue());
        usedMem.setValue(memData.usedMem.getValue());
        freeMem.setValue(memData.freeMem.getValue());
        maxMem.setValue(memData.maxMem.getValue());
        freePhMem.setValue(memData.freePhMem.getValue());
        totalPhMem.setValue(memData.totalPhMem.getValue());
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
}
