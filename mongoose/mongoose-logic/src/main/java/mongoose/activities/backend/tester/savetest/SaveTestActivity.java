package mongoose.activities.backend.tester.savetest;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class SaveTestActivity extends DomainPresentationActivityImpl<SaveTestPresentationModel> {

    public SaveTestActivity() {
        super(SaveTestPresentationViewActivity::new, SaveTestPresentationLogicActivity::new);
    }
}
