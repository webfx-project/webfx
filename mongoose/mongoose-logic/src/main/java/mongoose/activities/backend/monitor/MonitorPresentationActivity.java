package mongoose.activities.backend.monitor;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class MonitorPresentationActivity extends DomainPresentationActivityImpl<MonitorPresentationModel> {

    public MonitorPresentationActivity() {
        super(MonitorPresentationViewActivity::new, MonitorPresentationLogicActivity::new);
    }
}
