package mongoose.activities.backend.monitor;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class MonitorActivity extends DomainPresentationActivityImpl<MonitorPresentationModel> {

    MonitorActivity() {
        super(MonitorPresentationViewActivity::new, MonitorPresentationLogicActivity::new);
    }
}
