package mongoose.activities.tester.monitor;

import javafx.beans.property.ObjectProperty;
import mongoose.activities.tester.monitor.controller.SystemLookup;
import mongoose.activities.tester.monitor.controller.SystemLookupMock;
import mongoose.activities.tester.monitor.model.SysBean;
import mongoose.activities.tester.monitor.model.SysBeanFX;
import naga.core.spi.platform.Platform;
import naga.core.spi.toolkit.Toolkit;


/**
 * It should be only one monitor in the system, so it is designed as a singleton.
 * @author Jean-Pierre Alonso.
 */
public class Monitor {
    private static final Monitor instance = new Monitor();   // singleton

    private SystemLookup sysMon = new SystemLookupMock();
    private ObjectProperty<SysBeanFX> sbfx;
    private boolean cancelled;

    private Monitor() {}

    public static Monitor getInstance() {
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
