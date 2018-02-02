package mongoose.activities.shared.auth;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public class LoginRouting {

    private static String PATH = "/login";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PATH,
                false, false, LoginViewActivity::new, ViewDomainActivityContextFinal::new, null
        );
    }

    public static String getPath() {
        return PATH;
    }

}
