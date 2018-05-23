package mongoose.activities.backend.loadtester.drive.metrics.controller;

import mongoose.activities.backend.loadtester.drive.metrics.model.SysBean;

/**
 * @author Jean-Pierre Alonso.
 */
public class SystemLookupMock extends SystemLookup {

    @Override
    public SysBean snapshot() {
        return super.snapshot();
    }
}
