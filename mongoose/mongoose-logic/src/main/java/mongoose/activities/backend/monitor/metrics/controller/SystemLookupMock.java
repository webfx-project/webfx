package mongoose.activities.backend.monitor.metrics.controller;

import mongoose.activities.backend.monitor.metrics.model.SysBean;

/**
 * @author Jean-Pierre Alonso.
 */
public class SystemLookupMock extends SystemLookup {

    @Override
    public SysBean snapshot() {
        return super.snapshot();
    }
}
