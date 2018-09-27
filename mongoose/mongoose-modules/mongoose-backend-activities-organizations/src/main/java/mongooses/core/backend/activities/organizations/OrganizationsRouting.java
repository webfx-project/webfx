package mongooses.core.backend.activities.organizations;

import webfx.framework.activity.impl.combinations.domainpresentation.impl.DomainPresentationActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class OrganizationsRouting {

    private final static String PATH = "/organizations";

    public static String getPath() {
        return PATH;
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , OrganizationsActivity::new
                , DomainPresentationActivityContextFinal::new
        );
    }

    public static final class ProvidedUiRoute extends UiRouteImpl {
        public ProvidedUiRoute() {
            super(uiRoute());
        }
    }
}
