package mongoose.activities.shared.auth;

import naga.framework.activity.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import naga.framework.ui.router.UiRoute;
import naga.framework.ui.router.UiRouteImpl;

/**
 * @author Bruno Salmon
 */
public class UnauthorizedRouting {

    private static String PATH = "/unauthorized";

    public static UiRoute<?> uiRoute() {
        return new UiRouteImpl<>(PATH,
                false, false, UnauthorizedViewActivity::new, ViewDomainActivityContextFinal::new, null
        );
    }

    public static String getPath() {
        return PATH;
    }

}
