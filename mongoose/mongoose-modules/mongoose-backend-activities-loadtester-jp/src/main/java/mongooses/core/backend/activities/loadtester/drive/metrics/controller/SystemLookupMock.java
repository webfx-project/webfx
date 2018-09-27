package mongooses.core.backend.activities.loadtester.drive.metrics.controller;

import mongooses.core.backend.activities.loadtester.drive.metrics.model.SysBean;

/**
 * @author Jean-Pierre Alonso.
 */
public class SystemLookupMock extends SystemLookup {

    @Override
    public SysBean snapshot() {
        return super.snapshot();
    }
}
