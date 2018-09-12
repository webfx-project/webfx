package mongooses.loadtester.activities.loadtester.drive.metrics;

import javafx.beans.property.ObjectProperty;
import mongooses.loadtester.activities.loadtester.drive.metrics.controller.SystemLookup;
import mongooses.loadtester.activities.loadtester.drive.metrics.controller.SystemLookupMock;
import mongooses.loadtester.activities.loadtester.drive.metrics.model.MemData;
import mongooses.loadtester.activities.loadtester.drive.metrics.model.SysBean;
import webfx.platforms.core.services.scheduler.Scheduler;
import webfx.platforms.core.services.uischeduler.UiScheduler;


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
//                    Logger.log("elapsed time : " + (current - start) + " ms");
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
