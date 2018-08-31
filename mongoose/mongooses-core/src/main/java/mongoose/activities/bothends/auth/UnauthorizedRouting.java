package mongoose.activities.bothends.auth;

import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class UnauthorizedRouting {

    private static String PATH = "/unauthorized";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , false
                , UnauthorizedViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

}
