package mongoose.activities.backend.loadtester;

import naga.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class LoadTesterActivity extends DomainPresentationActivityImpl<LoadTesterPresentationModel> {

    LoadTesterActivity() {
        super(LoadTesterPresentationViewActivity::new, LoadTesterPresentationLogicActivity::new);
    }
}
