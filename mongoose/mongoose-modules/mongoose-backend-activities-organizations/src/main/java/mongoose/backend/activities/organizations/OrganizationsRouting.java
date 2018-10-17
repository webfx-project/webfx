package mongoose.backend.activities.organizations;

import webfx.framework.client.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.client.ui.uirouter.UiRoute;
import webfx.framework.client.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class OrganizationsRouting {

    private final static String PATH = "/organizations";

    public static String getPath() {
        return PATH;
    }

}
