package mongoose.activities.monitor.metrics.controller;

import mongoose.activities.monitor.metrics.model.SysBean;

/**
 * @author Jean-Pierre Alonso.
 */
public class SystemLookupMock extends SystemLookup {

    @Override
    public SysBean snapshot() {
        return super.snapshot();
    }
}
