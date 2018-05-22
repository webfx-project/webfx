package mongoose.activities.bothends.auth;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;

/**
 * @author Bruno Salmon
 */
public class LoginRouting {

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
