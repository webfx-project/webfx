package mongoose.backend.activities.loadtester.drive.metrics;

import javafx.beans.property.ObjectProperty;
import mongoose.backend.activities.loadtester.drive.metrics.controller.SystemLookup;
import mongoose.backend.activities.loadtester.drive.metrics.controller.SystemLookupMock;
import mongoose.backend.activities.loadtester.drive.metrics.model.MemData;
import mongoose.backend.activities.loadtester.drive.metrics.model.SysBean;
import webfx.platform.shared.services.scheduler.Scheduler;
import webfx.platform.client.services.uischeduler.UiScheduler;


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
        Scheduler.schedulePeriodic(1000, scheduled -> {
            long start = System.currentTimeMillis();
            long current;

            if (cancelled)
                scheduled.cancel();
            else {
                SysBean sb = sysMon.snapshot();
                current = System.currentTimeMillis();
                // Display results on the UI
                UiScheduler.scheduleDeferred(() -> memData.set(new MemData(sb)));
                // Display results on the console (optional)
                if (mode_console) {
                    sb.printState();
//                    Logger.log("elapsed time : " + (current - application) + " ms");
                }
            }
            cancelled = false;
        });
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
