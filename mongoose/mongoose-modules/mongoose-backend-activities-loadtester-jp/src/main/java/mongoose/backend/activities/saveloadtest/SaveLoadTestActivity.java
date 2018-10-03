package mongoose.backend.activities.saveloadtest;

import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class SaveLoadTestActivity extends DomainPresentationActivityImpl<SaveLoadTestPresentationModel> {

    SaveLoadTestActivity() {
        super(SaveLoadTestPresentationViewActivity::new, SaveLoadTestPresentationLogicActivity::new);
    }
}
