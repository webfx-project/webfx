package mongoose.activities.backend.organizations;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityImpl;

/**
 * @author Bruno Salmon
 */
final class OrganizationsActivity extends DomainPresentationActivityImpl<OrganizationsPresentationModel> {

    OrganizationsActivity() {
        super(OrganizationsPresentationViewActivity::new, OrganizationsPresentationLogicActivity::new);
    }
}
