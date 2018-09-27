package mongooses.core.sharedends.actvities.authz;

import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.ui.uirouter.ProvidedUnauthorizedUiRoute;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class UnauthorizedRouting {

    private static final String PATH = "/unauthorized";

    public static String getPath() {
        return PATH;
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , UnauthorizedViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static final class ProvidedUiRoute extends UiRouteImpl implements ProvidedUnauthorizedUiRoute {
        public ProvidedUiRoute() {
            super(uiRoute());
        }
    }

}
