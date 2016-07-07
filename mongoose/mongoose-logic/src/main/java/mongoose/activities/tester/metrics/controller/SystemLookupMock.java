package mongoose.activities.tester.metrics.controller;

import mongoose.activities.tester.metrics.model.SysBean;

/**
 * @author Jean-Pierre Alonso.
 */
public class SystemLookupMock extends SystemLookup {

    @Override
    public SysBean snapshot() {
        return super.snapshot();
    }
}
