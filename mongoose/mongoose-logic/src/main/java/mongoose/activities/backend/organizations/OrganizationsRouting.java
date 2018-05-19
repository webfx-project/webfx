package mongoose.activities.backend.organizations;

import naga.framework.activity.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class OrganizationsRouting {

    private final static String PATH = "/organizations";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , OrganizationsPresentationActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }
}
