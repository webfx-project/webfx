package mongooses.loadtester.activities.loadtester;

import webfx.framework.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class LoadTesterActivity extends DomainPresentationActivityImpl<LoadTesterPresentationModel> {

    LoadTesterActivity() {
        super(LoadTesterPresentationViewActivity::new, LoadTesterPresentationLogicActivity::new);
    }
}
