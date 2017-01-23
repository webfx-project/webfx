package mongoose.activities.backend.organizations;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
public class OrganizationsPresentationActivity extends DomainPresentationActivityImpl<OrganizationsPresentationModel> {

    public OrganizationsPresentationActivity() {
        super(OrganizationsPresentationViewActivity::new, OrganizationsPresentationLogicActivity::new);
    }
}
