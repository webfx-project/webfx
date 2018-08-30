package mongoose.activities.backend.monitor;

import naga.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class MonitorActivity extends DomainPresentationActivityImpl<MonitorPresentationModel> {

    MonitorActivity() {
        super(MonitorPresentationViewActivity::new, MonitorPresentationLogicActivity::new);
    }
}
