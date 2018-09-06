package mongooses.loadtester.activities.loadtester.drive.metrics.model;

import javafx.beans.property.Property;
import javafx.beans.property.SimpleObjectProperty;

/**
 * @author Jean-Pierre Alonso.
 */
public class MemData {
    private final Property<Long> committedMem   = new SimpleObjectProperty();
    private final Property<Long> totalMem       = new SimpleObjectProperty();
    private final Property<Long> usedMem        = new SimpleObjectProperty();
    private final Property<Long> freeMem        = new SimpleObjectProperty();
    private final Property<Long> maxMem         = new SimpleObjectProperty();
    private final Property<Long> freePhMem      = new SimpleObjectProperty();
    private final Property<Long> totalPhMem     = new SimpleObjectProperty();

    public MemData() {
        super();
    }

    public MemData(SysBean sb) {
        committedMem.setValue(sb.getCommittedMem());
        totalMem.setValue(sb.getTotalMem());
        usedMem.setValue(sb.getUsedMem());
        freeMem.setValue(sb.getFreeMem());
        maxMem.setValue(sb.getMaxMem());
        freePhMem.setValue(sb.getFreePhMem());
        totalPhMem.setValue(sb.getTotalPhMem());
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

    public Property<Long> committedMemProperty() {
        return committedMem;
    }

    public Property<Long> totalMemProperty() {
        return totalMem;
    }

    public Property<Long> usedMemProperty() {
        return usedMem;
    }

    public Property<Long> freeMemProperty() {
        return freeMem;
    }

    public Property<Long> maxMemProperty() {
        return maxMem;
    }

    public Property<Long> freePhMemProperty() {
        return freePhMem;
    }

    public Property<Long> totalPhMemProperty() {
        return totalPhMem;
    }
}
