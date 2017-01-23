package mongoose.activities.backend.tester.savetest;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class SaveTestPresentationActivity extends DomainPresentationActivityImpl<SaveTestPresentationModel> {

    public SaveTestPresentationActivity() {
        super(SaveTestPresentationViewActivity::new, SaveTestPresentationLogicActivity::new);
    }
}
