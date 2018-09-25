package mongooses.core.backend.activities.authorizations;

import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class AuthorizationsRouting {

    private static final String PATH = "/authorizations";

    public static String getPath() {
        return PATH;
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , true
                , AuthorizationsViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static final class ProvidedUiRoute extends UiRouteImpl {
        public ProvidedUiRoute() {
            super(uiRoute());
        }
    }
}
