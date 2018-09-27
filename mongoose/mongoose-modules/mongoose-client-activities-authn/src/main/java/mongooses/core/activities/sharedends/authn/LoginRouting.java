package mongooses.core.activities.sharedends.authn;

import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.ProvidedLoginUiRoute;
import webfx.framework.ui.uirouter.UiRoute;
import webfx.framework.ui.uirouter.impl.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public final class LoginRouting {

    private static final String PATH = "/login";

    public static String getPath() {
        return PATH;
    }

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , LoginViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static final class ProvidedUiRoute extends UiRouteImpl implements ProvidedLoginUiRoute {
        public ProvidedUiRoute() {
            super(uiRoute());
        }
    }

}
