package mongoose.activities.backend.tester;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class TesterActivity extends DomainPresentationActivityImpl<TesterPresentationModel> {

    TesterActivity() {
        super(TesterPresentationViewActivity::new, TesterPresentationLogicActivity::new);
    }
}
