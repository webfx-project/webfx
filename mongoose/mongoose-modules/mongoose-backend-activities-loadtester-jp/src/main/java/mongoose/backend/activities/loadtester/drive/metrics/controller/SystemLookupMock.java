package mongoose.backend.activities.loadtester.drive.metrics.controller;

import mongoose.backend.activities.loadtester.drive.metrics.model.SysBean;

/**
 * @author Jean-Pierre Alonso.
 */
public class SystemLookupMock extends SystemLookup {

    @Override
    public SysBean snapshot() {
        return super.snapshot();
    }
}
