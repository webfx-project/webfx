package mongoose.activities.backend.organizations;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class OrganizationsActivity extends DomainPresentationActivityImpl<OrganizationsPresentationModel> {

    OrganizationsActivity() {
        super(OrganizationsPresentationViewActivity::new, OrganizationsPresentationLogicActivity::new);
    }
}
