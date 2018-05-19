package mongoose.activities.backend.tester;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class TesterPresentationActivity extends DomainPresentationActivityImpl<TesterPresentationModel> {

    TesterPresentationActivity() {
        super(TesterPresentationViewActivity::new, TesterPresentationLogicActivity::new);
    }
}
