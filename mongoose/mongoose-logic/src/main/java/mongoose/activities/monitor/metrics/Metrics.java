package mongoose.activities.monitor.metrics;

import javafx.beans.property.ObjectProperty;
import mongoose.activities.monitor.metrics.controller.SystemLookup;
import mongoose.activities.monitor.metrics.controller.SystemLookupMock;
import mongoose.activities.monitor.metrics.model.SysBean;
import mongoose.activities.monitor.metrics.model.SysBeanFX;
import naga.platform.spi.Platform;
import naga.toolkit.spi.Toolkit;


/**
 * It should be only one monitor in the system, so it is designed as a singleton.
 * @author Jean-Pierre Alonso.
 */
public class Metrics {
    private static final Metrics instance = new Metrics();   // singleton

    private SystemLookup sysMon = new SystemLookupMock();
    private ObjectProperty<SysBeanFX> sbfx;
    private boolean cancelled;

    private Metrics() {}

    public static Metrics getInstance() {
        return instance;
    }

    public void start (boolean mode_console) {
        Platform.get().scheduler().schedulePeriodic(1000, () -> {
            long start = System.currentTimeMillis();
            long current;

            while (!cancelled) {
                SysBean sb = sysMon.snapshot();
                current = System.currentTimeMillis();
                // Display results on the console (optional)
                if (mode_console) {
                    sb.printState();
                    Platform.log("elapsed time : "+ (current - start)+" ms");
                }
                // Display results on the UI
                Toolkit.get().scheduler().scheduleDeferred(() -> sbfx.set(new SysBeanFX(sb)));
            }
            cancelled = false;
        });
    }

    public void stop () {
        cancelled = true;
    }

    public ObjectProperty<SysBeanFX> sbfxProperty() {
        return sbfx;
    }

    public void setSbfx(ObjectProperty<SysBeanFX> sbfx) {
        this.sbfx = sbfx;
    }
}
