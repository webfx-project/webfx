package mongoose.activities.backend.monitor.metrics;

import javafx.beans.property.ObjectProperty;
import mongoose.activities.backend.monitor.metrics.controller.SystemLookup;
import mongoose.activities.backend.monitor.metrics.controller.SystemLookupMock;
import mongoose.activities.backend.monitor.metrics.model.MemData;
import mongoose.activities.backend.monitor.metrics.model.SysBean;
import naga.commons.scheduler.Scheduled;
import naga.commons.util.tuples.Unit;
import naga.platform.spi.Platform;
import naga.toolkit.fx.spi.Toolkit;


/**
 * It should be only one monitor in the system, so it is designed as a singleton.
 * @author Jean-Pierre Alonso.
 */
public class Metrics {
    private static final Metrics instance = new Metrics();   // singleton

    private SystemLookup sysMon = new SystemLookupMock();
    private ObjectProperty<MemData> memData;
    private boolean cancelled;

    private Metrics() {}

    public static Metrics getInstance() {
        return instance;
    }

    public void start (boolean mode_console) {
        Unit<Scheduled> scheduledUnit = new Unit<>();
        scheduledUnit.set(Platform.get().scheduler().schedulePeriodic(1000, () -> {
            long start = System.currentTimeMillis();
            long current;

            if (cancelled)
                scheduledUnit.get().cancel();
            else {
                SysBean sb = sysMon.snapshot();
                current = System.currentTimeMillis();
                // Display results on the UI
                Toolkit.get().scheduler().scheduleDeferred(() -> memData.set(new MemData(sb)));
                // Display results on the console (optional)
                if (mode_console) {
                    sb.printState();
//                    Platform.log("elapsed time : " + (current - start) + " ms");
                }
            }
            cancelled = false;
        }));
    }

    public void stop () {
        cancelled = true;
    }

    public MemData getMemData() {
        return memData.get();
    }

    public ObjectProperty<MemData> memDataProperty() {
        return memData;
    }

    public void setMemData(ObjectProperty<MemData> memData) {
        this.memData = memData;
    }
}
