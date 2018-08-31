package mongoose.activities.backend.authorizations;

import webfx.framework.activity.base.combinations.viewdomain.impl.ViewDomainActivityContextFinal;
import webfx.framework.ui.uirouter.UiRoute;

/**
 * @author Bruno Salmon
 */
public final class AuthorizationsRouting {

    private static String PATH = "/authorizations";

    public static UiRoute<?> uiRoute() {
        return UiRoute.create(PATH
                , true
                , AuthorizationsViewActivity::new
                , ViewDomainActivityContextFinal::new
        );
    }

    public static String getPath() {
        return PATH;
    }

}
