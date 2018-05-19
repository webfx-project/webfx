package mongoose.activities.backend.organizations;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
class OrganizationsPresentationActivity extends DomainPresentationActivityImpl<OrganizationsPresentationModel> {

    OrganizationsPresentationActivity() {
        super(OrganizationsPresentationViewActivity::new, OrganizationsPresentationLogicActivity::new);
    }
}
