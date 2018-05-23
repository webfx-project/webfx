package mongoose.activities.backend.loadtester;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class LoadTesterActivity extends DomainPresentationActivityImpl<LoadTesterPresentationModel> {

    LoadTesterActivity() {
        super(LoadTesterPresentationViewActivity::new, LoadTesterPresentationLogicActivity::new);
    }
}
