package mongoose.activities.backend.organizations;

import naga.framework.activity.view.presentation.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class OrganizationsPresentationActivity extends DomainPresentationActivityImpl<OrganizationsPresentationModel> {

    public OrganizationsPresentationActivity() {
        super(OrganizationsPresentationViewActivity::new, OrganizationsPresentationLogicActivity::new);
    }
}
