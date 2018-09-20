package mongooses.core.activities.sharedends.auth;

import webfx.framework.activity.impl.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class LoginRouting {

    private static String PATH = "/login";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , LoginViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

}
