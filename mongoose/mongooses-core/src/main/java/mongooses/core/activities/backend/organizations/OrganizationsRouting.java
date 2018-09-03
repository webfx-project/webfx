package mongooses.core.activities.backend.organizations;

import webfx.framework.activity.base.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class OrganizationsRouting {

    private final static String PATH = "/organizations";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , OrganizationsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }
}
